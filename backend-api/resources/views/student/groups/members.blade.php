@extends('layouts.student')

@section('title', 'Manage Members - UniForum')

@section('page-title', 'Manage Members')

@section('content')

<div class="space-y-6">

    <!-- Header -->
    <div class="bg-white rounded-2xl border border-slate-200 p-6">
        <a href="{{ route('student.groups.show', $group->group_id) }}"
           class="text-sm text-blue-600 hover:underline">
            ← Back to Group
        </a>

        <h2 class="mt-4 text-2xl font-bold text-slate-800">
          {{ $group->group_name }} Discussion Group Members
        </h2>

        <p class="text-slate-500 mt-2">
            Manage group members, administrator privileges, warnings, and blacklisted users.
        </p>
    </div>

    <!-- Flash Messages -->
    @if(session('success'))
        <div class="p-4 bg-green-50 border border-green-200 text-green-700 rounded-xl text-sm font-medium">
            {{ session('success') }}
        </div>
    @endif

    @if($errors->has('group'))
        <div class="p-4 bg-red-50 border border-red-200 text-red-700 rounded-xl text-sm font-medium">
            {{ $errors->first('group') }}
        </div>
    @endif

    <!-- Members Table -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <div class="p-6 border-b border-slate-200">
            <h3 class="text-lg font-semibold text-slate-800">
                Group Members
            </h3>
        </div>

        <table class="w-full">
            <thead class="bg-slate-100">
                <tr>
                    <th class="text-left px-6 py-4 text-sm font-semibold text-slate-700">Name</th>
                    <th class="text-left px-6 py-4 text-sm font-semibold text-slate-700">User Role</th>
                    <th class="text-left px-6 py-4 text-sm font-semibold text-slate-700">Group Role</th>
                    <th class="text-left px-6 py-4 text-sm font-semibold text-slate-700">Status</th>
                    <th class="text-left px-6 py-4 text-sm font-semibold text-slate-700">Actions</th>
                </tr>
            </thead>

            <tbody class="divide-y divide-slate-100">
                @forelse($members as $member)
                <tr class="border-t">
                    <td class="px-6 py-5 text-sm font-medium text-slate-900">
                        {{ optional($member->user)->name ?? $member->name }}
                        @if(($member->user_id ?? $member->id) === $group->created_by)
                            <span class="ml-2 text-xs font-semibold px-2 py-0.5 rounded bg-amber-100 text-amber-800">Owner</span>
                        @endif
                    </td>

                    <td class="px-6 py-5 text-sm text-slate-600">
                        {{ ucfirst(optional($member->user)->role ?? $member->role ?? 'Student') }}
                    </td>

                    <td class="px-6 py-5">
                        @if(($member->role ?? optional($member->pivot)->role) === 'admin')
                           <span class="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">
                               Group Admin
                           </span>
                        @elseif(($member->role ?? optional($member->pivot)->role) === 'lecturer')
                            <span class="px-3 py-1 rounded-full bg-indigo-100 text-indigo-700 text-xs font-semibold">
                               Lecturer
                            </span>
                        @else
                            <span class="px-3 py-1 rounded-full bg-slate-100 text-slate-700 text-xs font-medium">
                               Member
                            </span>
                        @endif
                    </td>

                    <td class="px-6 py-5">
                        @if($member->blacklisted_until && \Carbon\Carbon::parse($member->blacklisted_until)->isFuture())
                            <span class="px-3 py-1 rounded-full bg-red-100 text-red-700 text-xs font-semibold">
                                Blacklisted
                            </span>
                        @elseif(($member->warning_count ?? 0) == 2)
                            <span class="px-3 py-1 rounded-full bg-amber-100 text-amber-700 text-xs font-semibold">
                                Warning 2
                            </span>
                        @elseif(($member->warning_count ?? 0) == 1)
                            <span class="px-3 py-1 rounded-full bg-yellow-100 text-yellow-700 text-xs font-semibold">
                                Warning 1
                            </span>
                        @else
                            <span class="px-3 py-1 rounded-full bg-emerald-100 text-emerald-700 text-xs font-semibold">
                                Active
                            </span>
                        @endif
                    </td>

                    <td class="px-6 py-5 space-x-3 text-sm">
                        {{-- Cannot perform management actions on the Group Owner --}}
                        @if(($member->user_id ?? $member->id) !== $group->created_by && ($member->user_id ?? $member->id) !== auth()->id())

                            {{-- Role Modification: Promote vs Demote --}}
                            @if(($member->role ?? optional($member->pivot)->role) === 'member')
                                <form method="POST" action="{{ route('student.groups.members.promote', ['group' => $group->group_id, 'user' => $member->user_id ?? $member->id]) }}" class="inline">
                                    @csrf
                                    <button type="submit" class="text-blue-600 hover:underline font-medium">
                                        Make Group Admin
                                    </button>
                                </form>
                            @elseif(($member->role ?? optional($member->pivot)->role) === 'admin')
                                <form method="POST" action="{{ route('student.groups.members.demote', ['group' => $group->group_id, 'user' => $member->user_id ?? $member->id]) }}" class="inline">
                                    @csrf
                                    <button type="submit" class="text-orange-600 hover:underline font-medium">
                                        Demote to Member
                                    </button>
                                </form>
                            @endif

                            {{-- Warnings --}}
                            @if(($member->warning_count ?? 0) < 2 && !($member->blacklisted_until && \Carbon\Carbon::parse($member->blacklisted_until)->isFuture()))
                                <form method="POST" action="{{ route('student.groups.members.warning', ['group' => $group->group_id, 'user' => $member->user_id ?? $member->id]) }}" class="inline">
                                    @csrf
                                    <button type="submit" class="text-yellow-600 hover:underline font-medium">
                                        Issue Warning
                                    </button>
                                </form>
                            @endif

                            {{-- Blacklist vs Reinstate --}}
                            @if(!$member->blacklisted_until || !\Carbon\Carbon::parse($member->blacklisted_until)->isFuture())
                                <form method="POST" action="{{ route('student.groups.members.blacklist', ['group' => $group->group_id, 'user' => $member->user_id ?? $member->id]) }}" class="inline">
                                    @csrf
                                    <button type="submit" class="text-red-600 hover:underline font-medium">
                                        Blacklist
                                    </button>
                                </form>
                            @else
                                <form method="POST" action="{{ route('student.groups.members.reinstate', ['group' => $group->group_id, 'user' => $member->user_id ?? $member->id]) }}" class="inline">
                                    @csrf
                                    <button type="submit" class="text-emerald-600 hover:underline font-medium">
                                        Reinstate
                                    </button>
                                </form>
                            @endif

                        @else
                            <span class="text-slate-400 text-xs italic">N/A</span>
                        @endif
                    </td>
                </tr>
                @empty
                <tr>
                    <td colspan="5" class="px-6 py-5 text-center text-slate-500 text-sm">
                        No members found in this group.
                    </td>
                </tr>
                @endforelse
            </tbody>
        </table>
    </div>

</div>

@endsection