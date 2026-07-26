"""
Shared text-cleaning logic.

IMPORTANT: this must produce identical output to whatever cleaning was used
when the model was trained. If training and inference clean text differently,
predictions silently degrade. Once you move your training script into this
project, replace its local clean_text()/strip_html() with:
    from text_utils import clean_text
"""
import re
import warnings

from bs4 import BeautifulSoup, MarkupResemblesLocatorWarning
import nltk

warnings.filterwarnings("ignore", category=MarkupResemblesLocatorWarning)

# Downloads are a no-op if already present locally.
nltk.download('stopwords', quiet=True)
nltk.download('wordnet', quiet=True)
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer

STOPWORDS = set(stopwords.words('english'))
LEMMATIZER = WordNetLemmatizer()


def strip_html(raw_html: str) -> str:
    soup = BeautifulSoup(raw_html or "", "html.parser")
    for code in soup.find_all("code"):
        code.decompose()
    return soup.get_text(separator=" ")


def clean_text(text: str) -> str:
    text = strip_html(text)
    text = text.lower()
    text = re.sub(r"http\S+|www\S+", " ", text)
    text = re.sub(r"[^a-z\s]", " ", text)
    text = re.sub(r"\s+", " ", text).strip()
    tokens = [
        LEMMATIZER.lemmatize(w) for w in text.split()
        if w not in STOPWORDS and len(w) > 2
    ]
    return " ".join(tokens)
