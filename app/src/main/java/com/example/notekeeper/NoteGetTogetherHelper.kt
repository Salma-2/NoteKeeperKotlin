package com.example.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(val context: Context, lifecycle : Lifecycle) : LifecycleObserver {
    private val tag = this::class.simpleName
    var currentLon = 0.0
    var currentLat = 0.0
    init {
        lifecycle.addObserver(this)
    }

    val locManager = PseudoLocationManager(context) { lat, lon ->
        currentLon = lon
        currentLat = lat
        Log.d(tag, "Location Callback Lat:$currentLat, Lon:$currentLon")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler() {
        locManager.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler() {
        locManager.stop()
    }
}