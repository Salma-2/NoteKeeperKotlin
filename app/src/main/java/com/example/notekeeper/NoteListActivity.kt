package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.adapters.NoteRecyclerAdapter
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*


class NoteListActivity : AppCompatActivity() {
    private val tag = this::class.simpleName



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(findViewById(R.id.toolbar))


        fab.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            startActivity(activityIntent)
        }

        list_items.layoutManager = LinearLayoutManager(this)
        list_items.adapter = NoteRecyclerAdapter(this, DataManager.loadNotes())
    }


    override fun onResume() {
        super.onResume()
        list_items.adapter?.notifyDataSetChanged()
    }
}