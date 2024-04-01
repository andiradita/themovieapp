package com.andiradita.themovieapp.ui.genre

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.MainActivity
import com.andiradita.themovieapp.adapter.GenreAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.GenreActivityBinding

class GenreActivity : AppCompatActivity() {
    private lateinit var binding: GenreActivityBinding
    private var adapter: GenreAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val viewModel: GenreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GenreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        observeGenreMovie()
        binding.tvContinue.setOnClickListener { view ->
            startActivity(Intent(view.context, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = GenreAdapter()
        layoutManager = GridLayoutManager(
            this, 2, GridLayoutManager.VERTICAL, false
        )
        binding.apply {
            rvGenre.adapter = adapter
            rvGenre.layoutManager = layoutManager
        }
    }

    private fun observeGenreMovie() {
        viewModel.getGenreMovies().observe(this) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        binding.progressIndicator.hide()
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        it.data?.let { genre -> adapter?.setGenre(genre) }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }
}