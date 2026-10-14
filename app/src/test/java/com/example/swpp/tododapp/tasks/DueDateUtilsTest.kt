package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {

    // Success cases

    @Test
    fun dueDateLabel_completedWithFutureDueDate_returnClear() {
        // GIVEN - A completed task due 2026-10-20, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 20)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - The label is CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnClear() {
        // GIVEN - A completed task due 2026-10-01, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 1)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN - The label is still CLEAR, not D+13
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnClear() {
        // GIVEN - A completed task with no due date, today is 2026-10-14
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(null, today, isCompleted = true)

        // THEN - The label is CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnEmpty() {
        // GIVEN - An active task with no due date, today is 2026-10-14
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(null, today, isCompleted = false)

        // THEN - The label is empty
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_dueToday_returnDDay() {
        // GIVEN - An active task due 2026-10-14, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 14)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueIn3Days_returnDMinus3() {
        // GIVEN - An active task due 2026-10-17, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 17)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-3
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_dueInMoreThanOneMonth_returnDMinus72() {
        // GIVEN - An active task due 2026-12-25, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 12, 25)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label counts every calendar day in between
        assertEquals("D-72", label)
    }

    @Test
    fun dueDateLabel_overdueBy3Days_returnDPlus3() {
        // GIVEN - An active task due 2026-10-11, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 11)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D+3
        assertEquals("D+3", label)
    }

    @Test
    fun dueDateLabel_overdueByMoreThanOneMonth_returnDPlus74() {
        // GIVEN - An active task due 2026-08-01, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 8, 1)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label counts every calendar day in between
        assertEquals("D+74", label)
    }

    // Edge cases: due today, across a year end

    @Test
    fun dueDateLabel_overdueByOneDayAcrossYearEnd_returnDPlus1() {
        // GIVEN - An active task due 2026-12-30, today is 2026-12-31
        val dueDate = LocalDate.of(2026, 12, 30)
        val today = LocalDate.of(2026, 12, 31)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D+1
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_dueTodayOnLastDayOfYear_returnDDay() {
        // GIVEN - An active task due 2026-12-31, today is 2026-12-31
        val dueDate = LocalDate.of(2026, 12, 31)
        val today = LocalDate.of(2026, 12, 31)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueTomorrowInNextYear_returnDMinus1() {
        // GIVEN - An active task due 2027-01-01, today is 2026-12-31
        val dueDate = LocalDate.of(2027, 1, 1)
        val today = LocalDate.of(2026, 12, 31)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-1
        assertEquals("D-1", label)
    }

    // Edge cases: one calendar month ahead

    @Test
    fun dueDateLabel_dueOneDayBeforeOneMonthAhead_returnDMinus30() {
        // GIVEN - An active task due 2026-11-13, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 11, 13)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-30
        assertEquals("D-30", label)
    }

    @Test
    fun dueDateLabel_dueExactlyOneMonthAhead_returnDMinus31() {
        // GIVEN - An active task due 2026-11-14, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 11, 14)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-31
        assertEquals("D-31", label)
    }

    @Test
    fun dueDateLabel_dueOneDayAfterOneMonthAhead_returnDMinus32() {
        // GIVEN - An active task due 2026-11-15, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 11, 15)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-32
        assertEquals("D-32", label)
    }

    // Edge cases: one calendar month overdue

    @Test
    fun dueDateLabel_overdueByOneDayLessThanOneMonth_returnDPlus29() {
        // GIVEN - An active task due 2026-09-15, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 9, 15)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D+29
        assertEquals("D+29", label)
    }

    @Test
    fun dueDateLabel_overdueByExactlyOneMonth_returnDPlus30() {
        // GIVEN - An active task due 2026-09-14, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 9, 14)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D+30
        assertEquals("D+30", label)
    }

    @Test
    fun dueDateLabel_overdueByOneDayMoreThanOneMonth_returnDPlus31() {
        // GIVEN - An active task due 2026-09-13, today is 2026-10-14
        val dueDate = LocalDate.of(2026, 9, 13)
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D+31
        assertEquals("D+31", label)
    }

    // Edge cases: leap day

    @Test
    fun dueDateLabel_dueDayBeforeLeapDay_returnDMinus1() {
        // GIVEN - An active task due 2028-02-28, today is 2028-02-27
        val dueDate = LocalDate.of(2028, 2, 28)
        val today = LocalDate.of(2028, 2, 27)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-1
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_dueOnLeapDay_returnDMinus2() {
        // GIVEN - An active task due 2028-02-29, today is 2028-02-27
        val dueDate = LocalDate.of(2028, 2, 29)
        val today = LocalDate.of(2028, 2, 27)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - The label is D-2
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_dueDayAfterLeapDay_returnDMinus3() {
        // GIVEN - An active task due 2028-03-01, today is 2028-02-27
        val dueDate = LocalDate.of(2028, 3, 1)
        val today = LocalDate.of(2028, 2, 27)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - February 29 is counted, so the label is D-3
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_dueMarch1InNonLeapYear_returnDMinus2() {
        // GIVEN - An active task due 2027-03-01, today is 2027-02-27
        val dueDate = LocalDate.of(2027, 3, 1)
        val today = LocalDate.of(2027, 2, 27)

        // WHEN - Build the label
        val label = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN - There is no February 29, so the label is D-2
        assertEquals("D-2", label)
    }
}
