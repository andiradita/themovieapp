package com.andiradita.themovieapp.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andiradita.themovieapp.databinding.ActivityDetailMovieBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}