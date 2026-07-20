<!DOCTYPE html>
<html>
<head>
    <title>Topic Export</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        .message { margin-bottom: 15px; border-bottom: 1px solid #ddd; padding-bottom: 10px; }
    </style>
</head>
<body>
    <h1>{{ $topic->title }}</h1>
    <p><strong>Created At:</strong> {{ $topic->created_at }}</p>
    <hr>
    <h3>Chat Stream:</h3>
    @foreach($messages as $message)
        <div class="message">
            <p><strong>User ID {{ $message->sender_id }}:</strong></p>
            <p>{{ $message->msg_txt }}</p>
            <small style="color: gray;">Sent at: {{ $message->posted_at }}</small>
        </div>
    @endforeach
</body>
</html>
