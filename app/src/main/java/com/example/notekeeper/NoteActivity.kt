package com.example.notekeeper

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_note.*


class NoteActivity : AppCompatActivity(){
    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET

    val noteGetTogetherHelper = NoteGetTogetherHelper(this, lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(findViewById(R.id.toolbar))


        val adapterCourses = ArrayAdapter<CourseInfo>(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_courses.adapter = adapterCourses



        notePosition =
            savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?: intent.getIntExtra(
                NOTE_POSITION,
                POSITION_NOT_SET
            )

        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        } else {
            createNewNote()
        }

        Log.d(tag, "onCreate")

    }

    private fun createNewNote() {
        DataManager.addNote(NoteInfo())
        notePosition = DataManager.loadNotes().lastIndex
    }

    private fun displayNote() {

        if (notePosition > DataManager.loadNotes().lastIndex) {
            showMessage("Note not found")
            Log.e(tag, "Invalid note position $notePosition," +
                    " max valid position ${DataManager.loadNotes().lastIndex}")
            return
        }
        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.loadNotes().get(notePosition)
        text_note_title.setText(note.title)
        text_note_text.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinner_courses.setSelection(coursePosition)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if (notePosition < DataManager.loadNotes().lastIndex) {
                    moveNext()
                } else {
                    val msg = "No more notes"
                    showMessage(msg)
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(msg: String) {
        Snackbar.make(text_note_title, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManager.loadNotes().lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            //mmkon tkon null le menu null , aw lw mafesh el item el bdwr 3leh
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
            }

        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        saveNote()
        Log.d(tag, "onPause")
        super.onPause()
    }


    private fun saveNote() {
        val note = DataManager.loadNote(notePosition)
        note.text = text_note_text.text.toString()
        note.title = text_note_title.text.toString()
        //returns ref to the selected course
        //cast ref to courseInfo
        note.course = spinner_courses.selectedItem as CourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
}
}