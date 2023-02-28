package com.example.rehab_mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val videoView = findViewById<VideoView>(R.id.video_view)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash_screen)

        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener {
            videoView.start()
        }

        // Since this is the launch activity, it will show the Splash Screen, delaying the intent to the MainActivity
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

}