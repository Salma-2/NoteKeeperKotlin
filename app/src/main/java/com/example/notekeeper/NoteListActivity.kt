package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*


class NoteListActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        list_notes.adapter = ArrayAdapter<NoteInfo>(
            this,
            android.R.layout.simple_list_item_1, DataManager.notes
        )

        list_notes.setOnItemClickListener{ parent, view, position, id ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, position)
            startActivity(activityIntent)
        }

        fab.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            startActivity(activityIntent)
        }

    }

    override fun onResume() {
        super.onResume()
        (list_notes.adapter as ArrayAdapter<NoteInfo>).notifyDataSetChanged()
    }
}