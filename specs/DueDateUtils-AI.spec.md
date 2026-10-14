# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Build the D-day label shown next to a task in the task list: `"CLEAR"` for a completed task, nothing for an active task without a due date, and otherwise how many calendar days are left until the due date (or have passed since it).

**Pre-conditions**
- `today` is the caller's current local date; the function does not read the clock itself.
- `dueDate` may be `null`, meaning the task has no due date.

**Post-conditions**
- Success cases
  - Completed task with a due date: returns `"CLEAR"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-20`, `today = 2026-10-14`, `isCompleted = true` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"CLEAR"`
  - Completed task with no due date: returns `"CLEAR"`. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, `isCompleted = true` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"CLEAR"`
  - Active task with no due date: returns `""`, so nothing is shown. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `""`
  - Active task due today: returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-Day"`
  - Active task due N days from today, less than one calendar month away: returns `"D-N"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-17`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-3"`
  - Active task due more than one calendar month from today: returns `"D-N"`, where N counts every calendar day in between, not only the days left over after whole months. `[Unit]`
    - Tip: Given `dueDate = 2026-12-25`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-72"`
  - Active task overdue by N days, less than one calendar month: returns `"D+N"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-11`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+3"`
  - Active task overdue by more than one calendar month: returns `"D+N"`, where N counts every calendar day in between. `[Unit]`
    - Tip: Given `dueDate = 2026-08-01`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+74"`
- Error cases
  - None. Every combination of arguments is valid.
- Edge cases
  - Overdue by one day, just before the overdue/due-today boundary, across a year end: returns `"D+1"`. `[Unit]`
    - Tip: Given `dueDate = 2026-12-30`, `today = 2026-12-31`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+1"`
  - Due today, at the boundary, on the last day of the year: returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-12-31`, `today = 2026-12-31`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-Day"`
  - Due tomorrow, just after the boundary, in the next year: returns `"D-1"`. `[Unit]`
    - Tip: Given `dueDate = 2027-01-01`, `today = 2026-12-31`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`
  - Due one day before the date exactly one calendar month ahead: returns `"D-30"`. `[Unit]`
    - Tip: Given `dueDate = 2026-11-13`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-30"`
  - Due exactly one calendar month ahead: returns `"D-31"`. `[Unit]`
    - Tip: Given `dueDate = 2026-11-14`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-31"`
  - Due one day after the date exactly one calendar month ahead: returns `"D-32"`. `[Unit]`
    - Tip: Given `dueDate = 2026-11-15`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-32"`
  - Overdue by one day less than exactly one calendar month: returns `"D+29"`. `[Unit]`
    - Tip: Given `dueDate = 2026-09-15`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+29"`
  - Overdue by exactly one calendar month: returns `"D+30"`. `[Unit]`
    - Tip: Given `dueDate = 2026-09-14`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+30"`
  - Overdue by one day more than exactly one calendar month: returns `"D+31"`. `[Unit]`
    - Tip: Given `dueDate = 2026-09-13`, `today = 2026-10-14`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D+31"`
  - Due the day before a leap day: returns `"D-1"`. `[Unit]`
    - Tip: Given `dueDate = 2028-02-28`, `today = 2028-02-27`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-1"`
  - Due on a leap day: returns `"D-2"`. `[Unit]`
    - Tip: Given `dueDate = 2028-02-29`, `today = 2028-02-27`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-2"`
  - Due the day after a leap day, so February 29 is counted: returns `"D-3"`. `[Unit]`
    - Tip: Given `dueDate = 2028-03-01`, `today = 2028-02-27`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-3"`
  - Due on March 1 in a non-leap year, so there is no February 29 to count: returns `"D-2"`. `[Unit]`
    - Tip: Given `dueDate = 2027-03-01`, `today = 2027-02-27`, `isCompleted = false` / When `dueDateLabel(dueDate, today, isCompleted)` is called / Then it returns `"D-2"`

**Undefined behavior** (optional)
- None. Every input has a defined label.
