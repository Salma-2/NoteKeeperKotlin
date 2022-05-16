package com.example.notekeeper

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateNewNote{
    //launch notelistactivity
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(ItemsActivity::class.java)
    @Test
    fun createNewNote(){
        val course= DataManager.courses["android_async"]
        val noteTitle = "This is a test note"
        val noteText = " This is the body of my test note"

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.spinner_courses)).perform(click())
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())


        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
        onView(withId(R.id.text_note_text)).perform(typeText(noteText))

//        closeSoftKeyboard()
        pressBack()
        pressBack()


        val newNote = DataManager.notes.last()
        assertEquals(course, newNote.course)
        assertEquals(noteTitle, newNote.title)
        assertEquals(noteText,newNote.text)

    }
}