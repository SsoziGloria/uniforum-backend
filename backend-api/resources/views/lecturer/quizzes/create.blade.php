@extends('layouts.lecturer')

@section('title', 'Create Quiz - UniForum')
@section('page-title', 'Create Quiz')

@section('content')
<div class="min-h-screen bg-slate-50">
    <div class="bg-white border-b border-slate-200">
        <div class="max-w-5xl mx-auto px-6 py-8">
            <a href="{{ route('lecturer.groups.quizzes.index', $group->group_id ?? $group->id) }}"
               class="text-sm text-blue-600 hover:underline flex items-center gap-2">
                ← Back to Quizzes
            </a>
            <h1 class="mt-5 text-3xl font-bold text-slate-900">Create New Quiz</h1>
            <p class="mt-2 text-slate-500">Configure schedule and add assessment questions.</p>
        </div>
    </div>

    <div class="max-w-5xl mx-auto px-6 py-10">
        <form method="POST" action="{{ route('lecturer.groups.quizzes.store', $group->group_id ?? $group->id) }}" class="space-y-8">
            @csrf

            <!-- Quiz Information -->
            <div class="bg-white rounded-2xl border border-slate-200 p-8">
                <h2 class="text-lg font-semibold text-slate-900 mb-6">Basic Information</h2>
                <div class="grid md:grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Quiz Title</label>
                        <input type="text" name="quiz_title" value="{{ old('quiz_title') }}" required placeholder="e.g. OOP Assessment 1"
                               class="w-full rounded-xl border-slate-200 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Student Category</label>
                        <input type="text" name="student_category" value="{{ old('student_category') }}" required placeholder="e.g. Software Engineering Year 2"
                               class="w-full rounded-xl border-slate-200 focus:ring-blue-500">
                    </div>
                </div>

                <div class="mt-6 grid md:grid-cols-3 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Start Date</label>
                        <input type="date" name="quiz_date" value="{{ old('quiz_date') }}" required class="w-full rounded-xl border-slate-200 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Start Time</label>
                        <input type="time" name="start_time" value="{{ old('start_time') }}" required class="w-full rounded-xl border-slate-200 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Duration (Minutes)</label>
                        <input type="number" name="duration_minutes" value="{{ old('duration_minutes', 30) }}" min="1" required class="w-full rounded-xl border-slate-200 focus:ring-blue-500">
                    </div>
                </div>
            </div>

            <!-- Dynamic Question Builder -->
            <div class="bg-white rounded-2xl border border-slate-200 p-8">
                <div class="flex justify-between items-center mb-6">
                    <div>
                        <h2 class="text-lg font-semibold text-slate-900">Quiz Questions</h2>
                        <p class="text-sm text-slate-500">Add questions, choices, and set the correct key.</p>
                    </div>
                    <button type="button" onclick="addQuestion()" class="px-4 py-2 bg-slate-100 text-blue-600 font-medium rounded-xl hover:bg-slate-200">
                        + Add Question
                    </button>
                </div>

                <div id="questions-container" class="space-y-6">
                    <!-- Default Question Row -->
                    <div class="question-card p-6 bg-slate-50 rounded-xl border border-slate-200" data-index="0">
                        <div class="flex justify-between items-center mb-4">
                            <h3 class="font-bold text-slate-700">Question 1</h3>
                            <div class="flex items-center gap-3">
                                <label class="text-xs text-slate-500 font-medium">Marks:</label>
                                <input type="number" name="questions[0][marks_worth]" value="1" min="1" class="w-20 rounded-lg border-slate-200 text-sm">
                            </div>
                        </div>

                        <textarea name="questions[0][qn_text]" rows="2" placeholder="Enter question description..." required
                                  class="w-full rounded-xl border-slate-200 mb-4 focus:ring-blue-500"></textarea>

                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Option A</label>
                                <input type="text" name="questions[0][options][A]" required placeholder="Option A text" class="w-full rounded-lg border-slate-200">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Option B</label>
                                <input type="text" name="questions[0][options][B]" required placeholder="Option B text" class="w-full rounded-lg border-slate-200">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Option C</label>
                                <input type="text" name="questions[0][options][C]" placeholder="Option C text" class="w-full rounded-lg border-slate-200">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Option D</label>
                                <input type="text" name="questions[0][options][D]" placeholder="Option D text" class="w-full rounded-lg border-slate-200">
                            </div>
                        </div>

                        <div class="mt-4">
                            <label class="block text-xs font-medium text-slate-500 mb-1">Correct Choice Key</label>
                            <select name="questions[0][correct_option]" class="w-48 rounded-lg border-slate-200 text-sm" required>
                                <option value="A">Option A</option>
                                <option value="B">Option B</option>
                                <option value="C">Option C</option>
                                <option value="D">Option D</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Submit Button -->
            <div class="flex justify-end gap-4">
                <a href="{{ route('lecturer.groups.quizzes.index', $group->group_id ?? $group->id) }}" class="px-6 py-3 rounded-xl bg-slate-100 text-slate-700 font-medium">Cancel</a>
                <button type="submit" class="px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700 font-medium">Publish Quiz</button>
            </div>
        </form>
    </div>
</div>

<script>
let qnIdx = 1;
function addQuestion() {
    const container = document.getElementById('questions-container');
    const html = `
        <div class="question-card p-6 bg-slate-50 rounded-xl border border-slate-200" data-index="${qnIdx}">
            <div class="flex justify-between items-center mb-4">
                <h3 class="font-bold text-slate-700">Question ${qnIdx + 1}</h3>
                <div class="flex items-center gap-3">
                    <label class="text-xs text-slate-500 font-medium">Marks:</label>
                    <input type="number" name="questions[${qnIdx}][marks_worth]" value="1" min="1" class="w-20 rounded-lg border-slate-200 text-sm">
                    <button type="button" onclick="this.closest('.question-card').remove()" class="text-red-500 text-xs hover:underline">Remove</button>
                </div>
            </div>

            <textarea name="questions[${qnIdx}][qn_text]" rows="2" placeholder="Enter question description..." required
                      class="w-full rounded-xl border-slate-200 mb-4 focus:ring-blue-500"></textarea>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-slate-500 mb-1">Option A</label>
                    <input type="text" name="questions[${qnIdx}][options][A]" required placeholder="Option A text" class="w-full rounded-lg border-slate-200">
                </div>
                <div>
                    <label class="block text-xs font-medium text-slate-500 mb-1">Option B</label>
                    <input type="text" name="questions[${qnIdx}][options][B]" required placeholder="Option B text" class="w-full rounded-lg border-slate-200">
                </div>
                <div>
                    <label class="block text-xs font-medium text-slate-500 mb-1">Option C</label>
                    <input type="text" name="questions[${qnIdx}][options][C]" placeholder="Option C text" class="w-full rounded-lg border-slate-200">
                </div>
                <div>
                    <label class="block text-xs font-medium text-slate-500 mb-1">Option D</label>
                    <input type="text" name="questions[${qnIdx}][options][D]" placeholder="Option D text" class="w-full rounded-lg border-slate-200">
                </div>
            </div>

            <div class="mt-4">
                <label class="block text-xs font-medium text-slate-500 mb-1">Correct Choice Key</label>
                <select name="questions[${qnIdx}][correct_option]" class="w-48 rounded-lg border-slate-200 text-sm" required>
                    <option value="A">Option A</option>
                    <option value="B">Option B</option>
                    <option value="C">Option C</option>
                    <option value="D">Option D</option>
                </select>
            </div>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', html);
    qnIdx++;
}
</script>
@endsection