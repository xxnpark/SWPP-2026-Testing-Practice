# Spec Refinement Report

1. DueDateUtils.spec.md: I added "completed task that is already overdue returns CLEAR" because the intent says CLEAR with or without a due date, but the AI spec never checked a completed task whose deadline had passed.
2. TasksListBindings.spec.md: I added "updated right after the task is checked or unchecked" to the Intent because it is an explicit requirement that the AI spec only implied.
3. TasksListBindings.spec.md: I added the edge case "task with no due date is checked" (hidden -> "CLEAR") because the AI spec had the uncheck step but not the check step.
4. TasksListBindings.spec.md: I added the edge case "completed task with a due date is unchecked" ("CLEAR" -> "D-3") so that all four check/uncheck x with/without due date combinations are covered.

I kept the rest of the AI spec because it already covered both target functions, ended every post-condition with a Tip, and had cases on both sides of every boundary.

