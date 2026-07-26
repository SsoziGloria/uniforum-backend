@extends('layouts.student')

@section('title', 'Take Quiz - ' . $quiz->quiz_title)
@section('page-title', $quiz->quiz_title)

@section('content')
<div id="quiz-container">
    <form id="quiz-form" action="{{ route('student.groups.quizzes.submit', ['group' => $group->group_id, 'quiz' => $quiz->quiz_id]) }}" method="POST">
        @csrf
        
        <!-- Quiz Sticky Header -->
        <div class="bg-white border-b border-slate-200 sticky top-0 z-20 shadow-sm">
            <div class="max-w-7xl mx-auto px-6 py-5 flex flex-col lg:flex-row justify-between items-center gap-4">
                <div>
                    <h1 class="text-2xl font-bold text-slate-900">
                        {{ $quiz->quiz_title }}
                    </h1>
                    <p class="text-slate-500 mt-1" id="question-tracker">
                        Question 1 of {{ count($questions) }}
                    </p>
                </div>

                <div class="flex items-center gap-4">
                    <div class="bg-red-100 text-red-700 px-5 py-3 rounded-xl font-mono font-bold text-lg" id="timer-display">
                        ⏱ 00:00:00
                    </div>

                    <button type="submit" onclick="return confirm('Are you sure you want to submit your quiz?')" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl font-medium">
                        Finish Quiz
                    </button>
                </div>
            </div>
        </div>

        <div class="max-w-7xl mx-auto px-6 py-10">
            <div class="grid lg:grid-cols-4 gap-8">

                <!-- Questions Area -->
                <div class="lg:col-span-3">
                    @foreach($questions as $index => $question)
                        <div class="question-block bg-white rounded-2xl border border-slate-200 p-8 {{ $index > 0 ? 'hidden' : '' }}" id="qn-card-{{ $index }}">
                            <span class="text-sm text-blue-600 font-semibold uppercase tracking-wider">
                                Question {{ $index + 1 }} ({{ $question->marks_worth }} {{ \Illuminate\Support\Str::plural('mark', $question->marks_worth) }})
                            </span>

                            <h2 class="mt-4 text-2xl font-bold text-slate-900">
                                {{ $question->qn_text }}
                            </h2>

                            <div class="mt-8 space-y-4">
                                @if(is_array($question->options))
                                    @foreach($question->options as $optKey => $optVal)
                                        <label class="flex items-center gap-4 border border-slate-200 rounded-xl p-4 hover:border-blue-500 cursor-pointer transition-all">
                                            <input type="radio" 
                                                   name="answers[{{ $question->quiz_qn_id }}]" 
                                                   value="{{ $optKey }}" 
                                                   class="qn-radio w-5 h-5 text-blue-600"
                                                   onchange="markAnswered({{ $index }})">
                                            <span class="text-slate-800 font-medium">{{ $optKey }}. {{ $optVal }}</span>
                                        </label>
                                    @endforeach
                                @endif
                            </div>

                            <div class="mt-10 flex justify-between">
                                <button type="button" 
                                        onclick="navigateQuestion({{ $index - 1 }})" 
                                        class="px-6 py-3 rounded-xl bg-slate-100 text-slate-600 hover:bg-slate-200 font-medium {{ $index === 0 ? 'invisible' : '' }}">
                                    Previous
                                </button>

                                @if($index < count($questions) - 1)
                                    <button type="button" 
                                            onclick="navigateQuestion({{ $index + 1 }})" 
                                            class="px-6 py-3 rounded-xl bg-blue-600 hover:bg-blue-700 text-white font-medium">
                                        Next Question
                                    </button>
                                @else
                                    <button type="submit" 
                                            onclick="return confirm('Ready to submit your quiz?')"
                                            class="px-6 py-3 rounded-xl bg-green-600 hover:bg-green-700 text-white font-medium">
                                        Submit All Answers
                                    </button>
                                @endif
                            </div>
                        </div>
                    @endforeach
                </div>

                <!-- Navigation Sidebar -->
                <div class="space-y-6">
                    <div class="bg-white rounded-2xl border border-slate-200 p-6">
                        <h2 class="font-semibold text-lg">Progress</h2>
                        <div class="mt-5">
                            <div class="flex justify-between text-sm">
                                <span>Answered</span>
                                <span id="progress-text">0 / {{ count($questions) }}</span>
                            </div>
                            <div class="mt-3 h-3 bg-slate-200 rounded-full overflow-hidden">
                                <div id="progress-bar" class="bg-blue-600 h-full w-0 transition-all"></div>
                            </div>
                        </div>
                    </div>

                    <div class="bg-white rounded-2xl border border-slate-200 p-6">
                        <h2 class="font-semibold text-lg mb-5">Question Navigator</h2>
                        <div class="grid grid-cols-5 gap-3">
                            @foreach($questions as $index => $question)
                                <button type="button" 
                                        id="nav-btn-{{ $index }}"
                                        onclick="navigateQuestion({{ $index }})" 
                                        class="rounded-lg h-10 font-bold transition-all bg-slate-100 text-slate-700 hover:bg-slate-200">
                                    {{ $index + 1 }}
                                </button>
                            @endforeach
                        </div>
                    </div>

                    <div class="bg-yellow-50 border border-yellow-200 rounded-2xl p-6">
                        <h2 class="font-semibold text-yellow-800">Quiz Rules</h2>
                        <ul class="mt-4 space-y-2 text-sm text-yellow-700 list-disc list-inside">
                            <li>Timer auto-submits when time reaches 0.</li>
                            <li>No extra time is awarded for late entries.</li>
                            <li>Do not navigate away or close your window.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    let currentIdx = 0;
    const totalQuestions = {{ count($questions) }};
    let remainingSeconds = {{ $remainingSeconds }};

    function navigateQuestion(targetIdx) {
        if (targetIdx < 0 || targetIdx >= totalQuestions) return;

        document.getElementById(`qn-card-${currentIdx}`).classList.add('hidden');
        document.getElementById(`qn-card-${targetIdx}`).classList.remove('hidden');

        currentIdx = targetIdx;
        document.getElementById('question-tracker').innerText = `Question ${currentIdx + 1} of ${totalQuestions}`;
        
        updateNavStyles();
    }

    function markAnswered(index) {
        updateNavStyles();
        updateProgress();
    }

    function updateNavStyles() {
        for (let i = 0; i < totalQuestions; i++) {
            const btn = document.getElementById(`nav-btn-${i}`);
            const qnCard = document.getElementById(`qn-card-${i}`);
            const hasChecked = qnCard.querySelector('input[type="radio"]:checked');

            btn.className = "rounded-lg h-10 font-bold transition-all ";

            if (i === currentIdx) {
                btn.className += "bg-blue-600 text-white ring-2 ring-blue-300";
            } else if (hasChecked) {
                btn.className += "bg-green-100 text-green-700 border border-green-300";
            } else {
                btn.className += "bg-slate-100 text-slate-700 hover:bg-slate-200";
            }
        }
    }

    function updateProgress() {
        let answeredCount = 0;
        for (let i = 0; i < totalQuestions; i++) {
            if (document.querySelector(`#qn-card-${i} input[type="radio"]:checked`)) {
                answeredCount++;
            }
        }
        document.getElementById('progress-text').innerText = `${answeredCount} / ${totalQuestions}`;
        const pct = (answeredCount / totalQuestions) * 100;
        document.getElementById('progress-bar').style.width = `${pct}%`;
    }

    // Countdown Timer Logic
    const timerInterval = setInterval(() => {
        if (remainingSeconds <= 0) {
            clearInterval(timerInterval);
            alert('Time is up! Submitting your answers automatically.');
            document.getElementById('quiz-form').submit();
            return;
        }

        remainingSeconds--;

        const hrs = Math.floor(remainingSeconds / 3600);
        const mins = Math.floor((remainingSeconds % 3600) / 60);
        const secs = remainingSeconds % 60;

        document.getElementById('timer-display').innerText = 
            `⏱ ${String(hrs).padStart(2, '0')}:${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    }, 1000);

    // Initial styling setup
    updateNavStyles();
</script>
@endsection