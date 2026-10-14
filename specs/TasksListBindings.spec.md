# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `@BindingAdapter("app:dueDateLabel") fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Show the bound task's D-day label (from `dueDateLabel`, using today's date) in a task row's due-date `TextView`, and hide that `TextView` when the task has no label, so the row always reflects the task currently bound to it, including right after the task is checked or unchecked.

**Pre-conditions**
- Called on the main thread, as data binding does when it binds a task row.
- `textView` may be new, or may still show the label of a task bound earlier: the list reuses rows, and a row is rebound in place when its task is checked, unchecked or edited.
- `task` may be `null`, which data binding passes before a row has a task.
- "Today" is the device's current local date (`LocalDate.now()`) at the time of the call. The function reads the clock itself, so the Tips give due dates as offsets from `LocalDate.now()`.

**Post-conditions**
- Success cases
  - Active task with a due date: the `TextView` shows the label and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-3"` and `textView.visibility` is `View.VISIBLE`
  - Completed task with a due date: the `TextView` shows `"CLEAR"` and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = true, dueDate = LocalDate.now().plusDays(3))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE`
  - Completed task with no due date: the `TextView` shows `"CLEAR"` and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = true, dueDate = null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE`
  - Active task with no due date: the `TextView` is hidden. `[Integration]`
    - Tip: Given a new `TextView(context)` (visible by default) and `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.visibility` is `View.GONE`
  - No task (`null`): the `TextView` is hidden. `[Integration]`
    - Tip: Given a new `TextView(context)` (visible by default) and `task = null` / When `setDueDateLabel(textView, null)` is called / Then `textView.visibility` is `View.GONE`
- Error cases
  - None. Every `TextView` and every task, including `null`, is valid.
- Edge cases
  - Overdue by one day by the device's local date: the `TextView` shows `"D+1"` and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().minusDays(1))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D+1"` and `textView.visibility` is `View.VISIBLE`
  - Due today by the device's local date: the `TextView` shows `"D-Day"` and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now())` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-Day"` and `textView.visibility` is `View.VISIBLE`
  - Due tomorrow by the device's local date: the `TextView` shows `"D-1"` and is visible. `[Integration]`
    - Tip: Given a new `TextView(context)` and `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(1))` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is `"D-1"` and `textView.visibility` is `View.VISIBLE`
  - Row rebound after its due date is cleared (label shown → hidden): the `TextView` is hidden. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3))`, so it shows `"D-3"` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = false, dueDate = null))` is called / Then `textView.visibility` is `View.GONE`
  - Row rebound after a task with no due date is checked (label hidden → shown): the `TextView` shows `"CLEAR"` and is visible. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = null)`, so it is `View.GONE` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = true, dueDate = null))` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE`
  - Row rebound after a completed task with no due date is unchecked (label shown → hidden): the `TextView` is hidden. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = true, dueDate = null)`, so it shows `"CLEAR"` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = false, dueDate = null))` is called / Then `textView.visibility` is `View.GONE`
  - Row rebound to no task (label shown → hidden): the `TextView` is hidden. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3))`, so it shows `"D-3"` / When `setDueDateLabel(textView, null)` is called / Then `textView.visibility` is `View.GONE`
  - Row rebound after a due date is set (label hidden → shown): the `TextView` shows the new label and is visible again. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = null)`, so it is `View.GONE` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3)))` is called / Then `textView.text` is `"D-3"` and `textView.visibility` is `View.VISIBLE`
  - Row rebound after a task with a due date is checked (one label replaced by another): the `TextView` shows `"CLEAR"` and stays visible. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3))`, so it shows `"D-3"` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = true, dueDate = LocalDate.now().plusDays(3)))` is called / Then `textView.text` is `"CLEAR"` and `textView.visibility` is `View.VISIBLE`
  - Row rebound after a completed task with a due date is unchecked (one label replaced by another): the `TextView` shows the D-day label again and stays visible. `[Integration]`
    - Tip: Given a `TextView(context)` already bound to `Task(title = "Title", description = "Desc", isCompleted = true, dueDate = LocalDate.now().plusDays(3))`, so it shows `"CLEAR"` / When `setDueDateLabel(textView, Task(title = "Title", description = "Desc", isCompleted = false, dueDate = LocalDate.now().plusDays(3)))` is called / Then `textView.text` is `"D-3"` and `textView.visibility` is `View.VISIBLE`

**Undefined behavior** (optional)
- The text of a hidden `TextView` is not specified: once it is `View.GONE` nothing is shown, whatever its text is.
- A call made exactly as local midnight passes may use either date as today, so a test that runs across midnight may see a label that is off by one day.
