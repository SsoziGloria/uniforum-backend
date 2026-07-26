<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>{{ $topic->title }} - Discussion Export</title>
    <style>
        body { font-family: sans-serif; margin: 20px; color: #0f172a; }
        .header { border-bottom: 2px solid #2563eb; padding-bottom: 15px; margin-bottom: 20px; }
        .title { font-size: 20px; font-weight: bold; margin: 0 0 5px 0; color: #0f172a; }
        .meta { font-size: 12px; color: #64748b; }
        .description { font-size: 13px; color: #334155; margin-top: 10px; line-height: 1.5; background: #f8fafc; padding: 10px; border-radius: 6px; }
        .question-card { border: 1px solid #cbd5e1; border-radius: 8px; padding: 12px; margin-bottom: 20px; page-break-inside: avoid; }
        .question-header { font-size: 11px; font-weight: bold; color: #1d4ed8; text-transform: uppercase; margin-bottom: 4px; }
        .author { font-weight: bold; font-size: 13px; color: #0f172a; }
        .question-text { font-size: 13px; margin-top: 6px; line-height: 1.4; color: #1e293b; }
    </style>
</head>
<body>
    <div class="header">
        <div class="title">{{ $topic->title }}</div>
        <div class="meta">
            Category: {{ $topic->ml_category ?? 'General' }} | Started by: {{ $topic->creator->name }} on {{ $topic->created_at->format('M d, Y') }}
        </div>
        @if($topic->description)
            <div class="description">
                <strong>Description:</strong> {{ $topic->description }}
            </div>
        @endif
    </div>

    <h3 style="font-size: 15px; color: #0f172a; margin-bottom: 15px;">Questions & Answers</h3>

    @forelse($messages as $question)
        <div class="question-card">
            <div class="question-header">Question</div>
            <div class="author">{{ $question->sender->name }} <span style="font-size: 11px; font-weight: normal; color: #64748b;">({{ $question->posted_at->format('M d, Y H:i') }})</span></div>
            <div class="question-text">{{ $question->msg_txt }}</div>

            @if($question->replies && $question->replies->count() > 0)
                <div style="margin-top: 12px;">
                    @foreach($question->replies as $reply)
                        @include('pdf._reply', ['reply' => $reply, 'question' => $question])
                    @endforeach
                </div>
            @endif
        </div>
    @empty
        <p style="font-size: 12px; color: #64748b;">No questions posted in this discussion.</p>
    @endforelse
</body>
</html>