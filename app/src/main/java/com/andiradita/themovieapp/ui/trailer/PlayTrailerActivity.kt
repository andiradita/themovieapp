package com.andiradita.themovieapp.ui.trailer

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.andiradita.themovieapp.databinding.ActivityPlayYoutubeBinding

class PlayTrailerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayYoutubeBinding

    companion object {
        const val trailerId = "trailerId"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val videoKey = intent.getStringExtra(trailerId).toString()

        binding.apply {
            with(wvYoutube) {
                val url = "https://www.youtube.com/watch?v=${videoKey}"
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl(url)
            }
        }
    }
}