package com.andiradita.themovieapp.ui.trailer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.adapter.MovieTrailerAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.ActivityTrailerBinding
import com.andiradita.themovieapp.listeners.OnTrailerClickListener

class TrailerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrailerBinding
    private val viewModel: TrailerViewModel by viewModels()
    private var adapter: MovieTrailerAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var moviePoster: String

    companion object {
        const val movieId = "movieId"
        const val poster = "poster"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra(movieId, 0)
        moviePoster = intent.getStringExtra(poster).toString()
        setupRecyclerView()
        observerTrailerMovie(id)
    }

    private fun setupRecyclerView() {
        adapter = MovieTrailerAdapter()
        adapter?.onTrailerListener(object : OnTrailerClickListener {
            override fun onClick(trailerUrl: String) {
                val intent = Intent(this@TrailerActivity, PlayTrailerActivity::class.java)
                intent.putExtra(PlayTrailerActivity.trailerId, trailerUrl)
                startActivity(intent)
            }
        })
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.apply {
            rvMovie.adapter = adapter
            rvMovie.layoutManager = layoutManager
        }
    }

    private fun observerTrailerMovie(id: Int) {
        viewModel.getTrailerMovie(id).observe(this) {
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
                        it.data.results.let { mov ->
                            if (mov != null) {
                                adapter?.setMovieList(mov, moviePoster)
                            }
                        }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }
}