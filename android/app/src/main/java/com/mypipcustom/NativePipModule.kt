package com.mypipcustom

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Rational
import android.widget.VideoView
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.util.Log
import android.content.Intent
import com.facebook.react.modules.core.DeviceEventManagerModule

class NativePipModule(private val reactContext: ReactApplicationContext) 
  : ReactContextBaseJavaModule(reactContext) {

    init {
        // Save the ReactApplicationContext instance for use in the companion object
        instance = reactContext
    }

    override fun getName() = "NativePipModule"

    @ReactMethod
    fun showPIP(urlVideo: String) {
        val activity = currentActivity
        // Check if the PiP WebView activity is already running
        if (activity is WebViewCustomActivity) {
            // Activity is already WebViewCustomActivity
            // You can recall the Pip with another video here
        } else {
            // Launch WebViewCustomActivity to start PiP mode
            val intent = Intent(activity, WebViewCustomActivity::class.java)
            intent.putExtra("url", urlVideo)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.startActivity(intent)
            return
        }
    }

    @ReactMethod
    fun bringAppToFront() {
        val activity = currentActivity
        if (activity != null) {
            // If an activity is in the foreground, lets call the MainActivity
            val intent = Intent(activity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            }
            activity.startActivity(intent)
        } else {
            // If no current activity (app in background), get the launch Intent and start it
            val packageManager = reactApplicationContext.packageManager
            val launchIntent = packageManager
                .getLaunchIntentForPackage(reactApplicationContext.packageName)
            launchIntent?.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
            reactApplicationContext.startActivity(launchIntent)
        }
    }

    @ReactMethod
    fun closeImmergoPip() {
        try {
            // Use the stored WebViewCustomActivity instance to close PiP
            WebViewCustomActivity.instance?.closePipActivity()
        } catch (e: Exception) {
            Log.e("NativePipModule", "Error while closing PiP activity: ${e.message}")
        }
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // No-op: required to prevent large warning messages in debug
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // No-op: required to prevent large warning messages in debug
    }

    companion object {
        // Store the ReactApplicationContext for sending events to JS
        private var instance: ReactApplicationContext? = null

        fun sendEvent(eventName: String, params: Any?) {
            instance
                ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                ?.emit(eventName, params)
        }
    }
}
