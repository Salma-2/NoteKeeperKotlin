package com.example.notekeeper

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//open or close drawer
import androidx.test.espresso.contrib.DrawerActions
//make selection mn el menu
import androidx.test.espresso.contrib.NavigationViewActions

import androidx.test.espresso.contrib.RecyclerViewActions
import com.jwhh.notekeeper.CourseRecyclerAdapter


@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @JvmField
    @Rule
    val ItemsActivity = ActivityTestRule(ItemsActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange() {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_courses))

        val coursePosition = 0
        onView(withId(R.id.list_items)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CourseRecyclerAdapter.ViewHolder>(
                coursePosition,
                click()
            )
        )


        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))

        val notePosition = 0
        onView(withId(R.id.list_items)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NoteRecyclerAdapter.ViewHolder>(
                notePosition,
                click()
            )
        )

        val note = DataManager.notes[notePosition]
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(note.course?.title))))
        onView(withId(R.id.text_note_title)).check(matches(withText(note.title)))
        onView(withId(R.id.text_note_text)).check(matches(withText(note.text)))






    }
}