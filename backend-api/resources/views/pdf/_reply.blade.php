<div style="margin-left: 20px; border-left: 2px solid #e2e8f0; padding-left: 12px; margin-top: 10px; margin-bottom: 10px;">
    <div style="font-size: 11px; color: #475569; font-weight: bold;">
        @if($reply->parent_msg_id == $question->msg_id)
            <span style="color: #15803d;">[ANSWER]</span>
        @else
            <span style="color: #ea580c;">[REPLY]</span>
        @endif
        {{ $reply->sender->name }} &bull; <span style="font-weight: normal; color: #64748b;">{{ $reply->posted_at->format('M d, Y H:i') }}</span>
    </div>

    <div style="font-size: 12px; color: #1e293b; margin-top: 4px; line-height: 1.4;">
        {{ $reply->msg_txt }}
    </div>

    @if($reply->replies && $reply->replies->count() > 0)
        @foreach($reply->replies as $childReply)
            @include('pdf._reply', ['reply' => $childReply, 'question' => $question])
        @endforeach
    @endif
</div>