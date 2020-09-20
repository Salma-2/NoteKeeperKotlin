package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.notekeeper.adapters.CourseRecyclerAdapter
import com.example.notekeeper.adapters.NoteRecyclerAdapter
import kotlinx.android.synthetic.main.activity_items.*
import kotlinx.android.synthetic.main.content_main.list_items


class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerAdapter.OnNoteSelectedListener {

    private val noteLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val noteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, DataManager.loadNotes())
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val courseLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(this, DataManager.courses.values.toList())
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(ItemsActivityViewModel::class.java)
    }

    private val recentlyViewedNoteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, viewModel.recentlyViewedNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            startActivity(activityIntent)
        }

        if (savedInstanceState != null && viewModel.isNewlyCreated) {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isNewlyCreated =false
        handleDisplayedSelection(viewModel.navDrawerDisplayedSelection)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            viewModel.saveState(outState)
        }

    }

    private fun displayNotes() {
        list_items.layoutManager = noteLayoutManager
        list_items.adapter = noteRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        list_items.layoutManager = courseLayoutManager
        list_items.adapter = courseRecyclerAdapter
    }

    private fun displayRecentlyViewedNotes() {
        list_items.layoutManager = noteLayoutManager
        list_items.adapter = recentlyViewedNoteRecyclerAdapter

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        list_items.adapter?.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_courses,
            R.id.nav_notes,
            R.id.nav_recently_viewed -> {
                handleDisplayedSelection(item.itemId)
                viewModel.navDrawerDisplayedSelection = item.itemId
            }

            R.id.nav_share -> {
                handleSelection("Share")
            }

            R.id.nav_send -> {
                handleSelection("Send")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplayedSelection(itemId: Int) {
        when (itemId) {
            R.id.nav_courses -> {
                displayCourses()
            }

            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_recently_viewed -> {
                displayRecentlyViewedNotes()
            }
        }

    }

    private fun handleSelection(msg: String) {
        Snackbar.make(list_items, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onNoteSelected(note: NoteInfo) {
        viewModel.addToRecentlyViewedNotes(note)
    }


}