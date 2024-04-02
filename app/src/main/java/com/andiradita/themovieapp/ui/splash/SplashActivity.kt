package com.andiradita.themovieapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.andiradita.themovieapp.MainActivity
import com.andiradita.themovieapp.databinding.ActivitySplashBinding
import com.andiradita.themovieapp.ui.genre.GenreActivity
import com.andiradita.themovieapp.utils.PreferencesHelper

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val checkGenre = PreferencesHelper.getGenreList(this@SplashActivity)
            if (checkGenre.isEmpty()) {
                startActivity(Intent(this, GenreActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 3000)
    }
}