package com.example.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel() {
    var isNewlyCreated = true
    var recentlyViewedNotesName =
        "com.example.notekeeper.ItemsActivityViewModel.recentlyViewedNotes"
    var navDrawerDisplayedSelectionName =
        "com.example.notekeeper.ItemsActivityViewModel.navDrawerDisplayedSelection"
    var navDrawerDisplayedSelection = R.id.nav_notes
    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }


    fun saveState(outState: Bundle) {
        outState.putInt(
            navDrawerDisplayedSelectionName,
            navDrawerDisplayedSelection
        )
        val noteIds = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState.putIntArray(recentlyViewedNotesName, noteIds)
    }

    fun restoreState(savedInstanceState: Bundle) {
        navDrawerDisplayedSelection =
            savedInstanceState.getInt(navDrawerDisplayedSelectionName)
        val noteIds = savedInstanceState.getIntArray(recentlyViewedNotesName)
        val noteList = DataManager.loadNotes(*noteIds!!)
        recentlyViewedNotes.addAll(noteList)
    }
}