package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private lateinit var textView: TextView

    @Before
    fun createTextView() {
        textView = TextView(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    private fun task(isCompleted: Boolean, dueDate: LocalDate?) =
        Task(title = "Title", description = "Desc", isCompleted = isCompleted, dueDate = dueDate)

    // Success cases

    @Test
    @UiThreadTest
    fun setDueDateLabel_activeTaskDueIn3Days_showDMinus3() {
        // GIVEN - A new TextView and an active task due in 3 days
        val task = task(isCompleted = false, dueDate = LocalDate.now().plusDays(3))

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows D-3
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_completedTaskWithDueDate_showClear() {
        // GIVEN - A new TextView and a completed task due in 3 days
        val task = task(isCompleted = true, dueDate = LocalDate.now().plusDays(3))

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows CLEAR
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_completedTaskWithoutDueDate_showClear() {
        // GIVEN - A new TextView and a completed task with no due date
        val task = task(isCompleted = true, dueDate = null)

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows CLEAR
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_activeTaskWithoutDueDate_hideLabel() {
        // GIVEN - A new TextView (visible by default) and an active task with no due date
        val task = task(isCompleted = false, dueDate = null)

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label is hidden
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_nullTask_hideLabel() {
        // GIVEN - A new TextView (visible by default) and no task

        // WHEN - Bind null
        setDueDateLabel(textView, null)

        // THEN - The label is hidden
        assertEquals(View.GONE, textView.visibility)
    }

    // Edge cases: due date around today by the device's local date

    @Test
    @UiThreadTest
    fun setDueDateLabel_overdueByOneDay_showDPlus1() {
        // GIVEN - A new TextView and an active task due yesterday
        val task = task(isCompleted = false, dueDate = LocalDate.now().minusDays(1))

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows D+1
        assertEquals("D+1", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_dueToday_showDDay() {
        // GIVEN - A new TextView and an active task due today
        val task = task(isCompleted = false, dueDate = LocalDate.now())

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows D-Day
        assertEquals("D-Day", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_dueTomorrow_showDMinus1() {
        // GIVEN - A new TextView and an active task due tomorrow
        val task = task(isCompleted = false, dueDate = LocalDate.now().plusDays(1))

        // WHEN - Bind the task
        setDueDateLabel(textView, task)

        // THEN - The label shows D-1
        assertEquals("D-1", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    // Edge cases: a row rebound in place

    @Test
    @UiThreadTest
    fun setDueDateLabel_dueDateCleared_hideLabel() {
        // GIVEN - A TextView already showing D-3
        setDueDateLabel(textView, task(isCompleted = false, dueDate = LocalDate.now().plusDays(3)))

        // WHEN - Rebind the task after its due date is cleared
        setDueDateLabel(textView, task(isCompleted = false, dueDate = null))

        // THEN - The label is hidden
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_taskWithoutDueDateChecked_showClear() {
        // GIVEN - A TextView already bound to an active task with no due date
        setDueDateLabel(textView, task(isCompleted = false, dueDate = null))

        // WHEN - Rebind the task after it is checked
        setDueDateLabel(textView, task(isCompleted = true, dueDate = null))

        // THEN - The label shows CLEAR
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_completedTaskWithoutDueDateUnchecked_hideLabel() {
        // GIVEN - A TextView already showing CLEAR for a completed task with no due date
        setDueDateLabel(textView, task(isCompleted = true, dueDate = null))

        // WHEN - Rebind the task after it is unchecked
        setDueDateLabel(textView, task(isCompleted = false, dueDate = null))

        // THEN - The label is hidden
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_reboundToNullTask_hideLabel() {
        // GIVEN - A TextView already showing D-3
        setDueDateLabel(textView, task(isCompleted = false, dueDate = LocalDate.now().plusDays(3)))

        // WHEN - Rebind with no task
        setDueDateLabel(textView, null)

        // THEN - The label is hidden
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_dueDateSet_showDMinus3() {
        // GIVEN - A TextView already bound to an active task with no due date
        setDueDateLabel(textView, task(isCompleted = false, dueDate = null))

        // WHEN - Rebind the task after a due date 3 days away is set
        setDueDateLabel(textView, task(isCompleted = false, dueDate = LocalDate.now().plusDays(3)))

        // THEN - The label shows D-3
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_taskWithDueDateChecked_showClear() {
        // GIVEN - A TextView already showing D-3
        setDueDateLabel(textView, task(isCompleted = false, dueDate = LocalDate.now().plusDays(3)))

        // WHEN - Rebind the task after it is checked
        setDueDateLabel(textView, task(isCompleted = true, dueDate = LocalDate.now().plusDays(3)))

        // THEN - The label shows CLEAR
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    @UiThreadTest
    fun setDueDateLabel_completedTaskWithDueDateUnchecked_showDMinus3() {
        // GIVEN - A TextView already showing CLEAR for a completed task due in 3 days
        setDueDateLabel(textView, task(isCompleted = true, dueDate = LocalDate.now().plusDays(3)))

        // WHEN - Rebind the task after it is unchecked
        setDueDateLabel(textView, task(isCompleted = false, dueDate = LocalDate.now().plusDays(3)))

        // THEN - The label shows D-3 again
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }
}
