"""
ml-service — the small Python API that sits between Laravel and your trained
model, exactly as described in the repo design doc:

    Laravel (RecommendationClient.php) --HTTP--> this service --> model

Two jobs:
  1. POST /predict-topic        — classify a new discussion's topic
  2. POST /index-discussion     — add a discussion to the recommendation
                                   index (call this every time a discussion
                                   is created in Laravel)
  3. POST /recommendations      — given a user's seen discussion IDs,
                                   return the most similar unseen ones

Why /index-discussion exists: your forum has zero real discussions right
now (cold start). Rather than hard-coding the Stack Overflow dataset into
this service, Laravel feeds it real discussions as they're created, so the
recommendation index grows from real data from day one. Laravel should
call /index-discussion right after saving any new discussion post.

Run locally:
    pip install -r requirements.txt
    uvicorn api.app:app --reload --port 8000

Test it:
    curl http://localhost:8000/health
"""
import os
from pathlib import Path
from typing import List, Optional

import joblib
import numpy as np
from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel, Field
from sklearn.metrics.pairwise import cosine_similarity

import sys
sys.path.append(str(Path(__file__).resolve().parent.parent))
from text_utils import clean_text

BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_PATH = BASE_DIR / "models" / "topic_classifier.joblib"
VECTORIZER_PATH = BASE_DIR / "models" / "tfidf_vectorizer.joblib"
INDEX_STORE_PATH = BASE_DIR / "models" / "index_store.joblib"

# Set this env var in production; if unset, the API is open (fine for local dev).
API_KEY = os.environ.get("ML_SERVICE_API_KEY")

app = FastAPI(title="Smart Discussion Forum — Recommendation Service", version="1.0")


# ─── Load trained artifacts ────────────────────────────────────────────────
def load_model():
    if not MODEL_PATH.exists() or not VECTORIZER_PATH.exists():
        raise RuntimeError(
            f"Model files not found. Expected:\n  {MODEL_PATH}\n  {VECTORIZER_PATH}\n"
            "Run your training script first and copy the two .joblib files into ml-service/models/."
        )
    model = joblib.load(MODEL_PATH)
    vectorizer = joblib.load(VECTORIZER_PATH)
    return model, vectorizer


model, vectorizer = load_model()


from sklearn.feature_extraction.text import TfidfVectorizer

# ─── Simple persisted recommendation index ─────────────────────────────────
class DiscussionIndex:
    """
    In-memory index of {id, title, topic, text}, persisted to disk so it
    survives service restarts.

    IMPORTANT: this keeps its OWN TfidfVectorizer, separate from the one
    used for topic classification. The classifier's vectorizer was trained
    on the Stack Overflow proxy dataset and doesn't recognise real forum
    vocabulary (e.g. "laravel", "eloquent"), which would make every
    similarity score come out as 0.0. This index instead re-fits a small
    vectorizer directly on the real discussions as they're indexed, so it
    always understands your actual forum content. Refitting on every add
    is a bit wasteful but is fine at university-forum scale (thousands of
    discussions, not millions).
    """

    def __init__(self, path: Path):
        self.path = path
        self.entries: List[dict] = []   # {id, title, topic, text}
        self.vectorizer: Optional[TfidfVectorizer] = None
        self.matrix = None
        self._load()

    def _load(self):
        if self.path.exists():
            self.entries = joblib.load(self.path)
            self._refit()

    def _save(self):
        joblib.dump(self.entries, self.path)

    def _refit(self):
        if len(self.entries) < 2:
            self.vectorizer = None
            self.matrix = None
            return
        self.vectorizer = TfidfVectorizer(max_features=5000, ngram_range=(1, 2), min_df=1)
        texts = [e["text"] for e in self.entries]
        self.matrix = self.vectorizer.fit_transform(texts)

    def add(self, discussion_id: int, title: str, body: str, topic: str):
        text = clean_text(title) + " " + clean_text(body)
        # Replace if this ID was already indexed (e.g. post was edited)
        self.entries = [e for e in self.entries if e["id"] != discussion_id]
        self.entries.append({"id": discussion_id, "title": title, "topic": topic, "text": text})
        self._refit()
        self._save()

    def remove(self, discussion_id: int):
        before = len(self.entries)
        self.entries = [e for e in self.entries if e["id"] != discussion_id]
        self._refit()
        self._save()
        return len(self.entries) != before

    def recommend(self, seen_ids: List[int], top_n: int = 5):
        if self.matrix is None:
            return []  # not enough indexed discussions yet — Laravel should fall back to "popular/latest"

        seen_positions = [i for i, e in enumerate(self.entries) if e["id"] in seen_ids]
        candidate_positions = [i for i, e in enumerate(self.entries) if e["id"] not in seen_ids]

        if not seen_positions or not candidate_positions:
            return []

        user_profile = self.matrix[seen_positions].mean(axis=0)
        user_profile = np.asarray(user_profile)

        candidate_matrix = self.matrix[candidate_positions]
        sims = cosine_similarity(user_profile, candidate_matrix).flatten()

        ranked = sorted(zip(candidate_positions, sims), key=lambda pair: pair[1], reverse=True)
        return [
            {
                "id": self.entries[pos]["id"],
                "title": self.entries[pos]["title"],
                "topic": self.entries[pos]["topic"],
                "similarity": float(sim),
            }
            for pos, sim in ranked[:top_n]
        ]


index = DiscussionIndex(INDEX_STORE_PATH)


# ─── Auth helper ────────────────────────────────────────────────────────────
def check_api_key(x_api_key: Optional[str]):
    if API_KEY and x_api_key != API_KEY:
        raise HTTPException(status_code=401, detail="Invalid or missing API key")


# ─── Request/response schemas ──────────────────────────────────────────────
class PredictTopicRequest(BaseModel):
    title: str
    body: str = ""


class PredictTopicResponse(BaseModel):
    topic: str


class IndexDiscussionRequest(BaseModel):
    id: int
    title: str
    body: str = ""


class IndexDiscussionResponse(BaseModel):
    id: int
    topic: str
    indexed: bool = True


class RecommendationRequest(BaseModel):
    seen_ids: List[int] = Field(default_factory=list)
    top_n: int = 5


class RecommendationItem(BaseModel):
    id: int
    title: str
    topic: str
    similarity: float


# ─── Endpoints ──────────────────────────────────────────────────────────────
@app.get("/health")
def health():
    return {"status": "ok", "indexed_discussions": len(index.entries)}


@app.post("/predict-topic", response_model=PredictTopicResponse)
def predict_topic(req: PredictTopicRequest, x_api_key: Optional[str] = Header(None)):
    check_api_key(x_api_key)
    text = clean_text(req.title) + " " + clean_text(req.body)
    vector = vectorizer.transform([text])
    topic = model.predict(vector)[0]
    return {"topic": topic}


@app.post("/index-discussion", response_model=IndexDiscussionResponse)
def index_discussion(req: IndexDiscussionRequest, x_api_key: Optional[str] = Header(None)):
    check_api_key(x_api_key)
    text = clean_text(req.title) + " " + clean_text(req.body)
    vector = vectorizer.transform([text])
    topic = model.predict(vector)[0]
    index.add(req.id, req.title, req.body, topic)
    return {"id": req.id, "topic": topic, "indexed": True}


@app.delete("/index-discussion/{discussion_id}")
def delete_discussion(discussion_id: int, x_api_key: Optional[str] = Header(None)):
    check_api_key(x_api_key)
    removed = index.remove(discussion_id)
    if not removed:
        raise HTTPException(status_code=404, detail="Discussion not found in index")
    return {"id": discussion_id, "removed": True}


@app.post("/recommendations", response_model=List[RecommendationItem])
def recommendations(req: RecommendationRequest, x_api_key: Optional[str] = Header(None)):
    check_api_key(x_api_key)
    return index.recommend(req.seen_ids, req.top_n)