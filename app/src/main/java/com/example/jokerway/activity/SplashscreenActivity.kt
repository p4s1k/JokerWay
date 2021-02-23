package com.example.jokerway.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import com.example.jokerway.R

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val asteroids = findViewById<ImageView>(R.id.group50)

        asteroids.animate().apply {
            duration = 4000
            rotation(360f)

        }

        Handler().postDelayed({
            val intent = Intent(this, FragmentContainerActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

}