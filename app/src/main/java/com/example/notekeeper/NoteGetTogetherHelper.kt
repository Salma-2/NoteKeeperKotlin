package com.example.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(val context: Context, lifecycle: Lifecycle) : LifecycleObserver {
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
    val msgManager = PseudoMessagingManager(context)
    var msgConnection: PseudoMessagingConnection? = null


    fun sendMessage(note: NoteInfo) {
        val getTogetherMessage = "Lat:$currentLat | Lon:$currentLon |" +
                " Note title: ${note.title} | Note Text: ${note.text}"
        msgConnection?.send(getTogetherMessage)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler() {
        Log.d(tag, "startHandler")
        locManager.start()
        msgManager.connect() { connection ->
            msgConnection = connection
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler() {
        Log.d(tag, "stopHandler")
        locManager.stop()
        msgConnection?.disconnect()
    }
}