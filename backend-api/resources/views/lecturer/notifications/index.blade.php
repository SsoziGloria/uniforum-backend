@extends('layouts.lecturer')

@section('title', 'Notifications - UniForum')

@section('page-title', 'Notifications')

@section('content')

<div>
    <!-- Header -->
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-6xl mx-auto px-6 py-8 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h1 class="text-3xl font-bold text-slate-900">
                    Notifications
                </h1>
                <p class="mt-2 text-slate-500">
                    Stay updated with student submissions, replies, and activities across your groups.
                </p>
            </div>

            @if($unreadCount > 0)
                <form action="{{ route('lecturer.notifications.read-all') }}" method="POST">
                    @csrf
                    <button type="submit" class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 text-sm font-medium rounded-xl transition">
                        Mark all as read ({{ $unreadCount }})
                    </button>
                </form>
            @endif
        </div>
    </div>

    <div class="max-w-6xl mx-auto px-6 py-8">
        <div class="space-y-4">
            @forelse($notifications as $notification)
                @php
                    $data = $notification->data;
                    $isUnread = is_null($notification->read_at);
                    $isWarning = ($data['type'] ?? '') === 'warning';
                @endphp

                <div class="{{ $isWarning ? 'bg-red-50 border-red-200' : ($isUnread ? 'bg-blue-50/40 border-blue-200' : 'bg-white border-slate-200') }} rounded-2xl border p-6 hover:shadow-md transition">
                    <div class="flex justify-between items-start gap-4">
                        <div class="flex gap-4">
                            <div class="w-12 h-12 rounded-full {{ $isWarning ? 'bg-red-100' : 'bg-blue-100' }} flex items-center justify-center shrink-0 text-xl">
                                {{ $data['icon'] ?? '🔔' }}
                            </div>

                            <div>
                                <h2 class="font-semibold {{ $isWarning ? 'text-red-700' : 'text-slate-900' }}">
                                    {{ $data['title'] ?? 'Notification' }}
                                </h2>

                                <p class="{{ $isWarning ? 'text-red-600' : 'text-slate-600' }} mt-1 text-sm">
                                    {{ $data['message'] ?? '' }}
                                </p>

                                <div class="flex items-center gap-4 mt-3">
                                    <span class="text-xs {{ $isWarning ? 'text-red-500' : 'text-slate-400' }}">
                                        {{ $notification->created_at->diffForHumans() }}
                                    </span>

                                    @if(isset($data['link']) && $data['link'] !== '#')
                                        <a href="{{ $data['link'] }}" class="text-xs font-semibold text-blue-600 hover:underline">
                                            View Details &rarr;
                                        </a>
                                    @endif
                                </div>
                            </div>
                        </div>

                        <div class="flex items-center gap-2">
                            @if($isUnread)
                                <span class="w-2.5 h-2.5 rounded-full bg-blue-600" title="Unread"></span>
                                <form action="{{ route('lecturer.notifications.read', $notification->id) }}" method="POST">
                                    @csrf
                                    <button type="submit" class="text-xs text-slate-400 hover:text-slate-600">
                                        Mark read
                                    </button>
                                </form>
                            @endif
                        </div>
                    </div>
                </div>
            @empty
                <div class="bg-white rounded-2xl border border-slate-200 p-12 text-center">
                    <div class="text-4xl mb-3">🔔</div>
                    <h3 class="text-lg font-semibold text-slate-900">No notifications yet</h3>
                    <p class="text-slate-500 text-sm mt-1">You're all caught up! Check back later for updates from your students and groups.</p>
                </div>
            @endforelse

            <!-- Pagination Links -->
            <div class="mt-6">
                {{ $notifications->links() }}
            </div>
        </div>
    </div>
</div>

@endsection