package com.mypipcustom

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewCustomActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    companion object {
        var instance: WebViewCustomActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Store the instance for use in NativePipModule
        instance = this
        setContentView(R.layout.activity_pip)

        // Initialize WebView and enable settings
        webView = findViewById(R.id.video_view_pip)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true  // Enable JavaScript if needed
        webView.settings.mediaPlaybackRequiresUserGesture = false

        // Load the URL (can be passed via Intent)
        val url = intent.getStringExtra("url") ?: ""
        webView.loadUrl(url)

        // On Android versions below 14, immediately enter PiP
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    // Enter PiP on Android 14+ with auto-enter enabled
    private fun enterPip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .setAutoEnterEnabled(true)  // Android 14 feature
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    // Close the PiP activity from native module
    fun closePipActivity() {
        instance?.finish()  // Finish the PiP activity
    }

    override fun onDestroy() {
        // PiP has been destroyed
        // Notify React Native that PiP was destroyed
        NativePipModule.sendEvent("onPipDestroyed", null)
        super.onDestroy()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (!isInPictureInPictureMode) {
            // PiP mode has closed: notify React Native and return to main Activity
            NativePipModule.sendEvent("onExitPip", null)

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // On Android 14+, re-enter PiP if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            enterPip()
        }
    }
}
