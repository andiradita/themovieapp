package com.andiradita.themovieapp.ui.trailer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.MovieTrailerAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.ActivityTrailerBinding
import com.andiradita.themovieapp.listeners.OnTrailerClickListener
import com.andiradita.themovieapp.utils.Constants

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

        title = Constants.TITLE_MENU_TRAILER
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(movieId, 0)
        moviePoster = intent.getStringExtra(poster).toString()
        setupRecyclerView()
        observerTrailerMovie(id)
        binding.layoutError.btnRetry.setOnClickListener {
            observerTrailerMovie(id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
                binding.apply {
                    when (it) {
                        is LoadingState.Loading -> {
                            handleVisibilityLayout(View.VISIBLE, View.GONE)
                            progressIndicator.show()
                        }

                        is LoadingState.Error -> {
                            handleVisibilityLayout(View.GONE, View.VISIBLE)
                            handleErrorLayout(it.errorMessage)
                            progressIndicator.hide()
                            Toast.makeText(this@TrailerActivity, it.errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }

                        is LoadingState.Success -> {
                            it.data.results.let { mov ->
                                if (mov != null) {
                                    if (mov.isEmpty()) {
                                        handleVisibilityLayout(View.GONE, View.VISIBLE)
                                        handleErrorLayout("Data not available!", true)
                                    } else {
                                        handleVisibilityLayout(View.VISIBLE, View.GONE)
                                        adapter?.setMovieList(mov, moviePoster)
                                    }

                                }
                            }
                            progressIndicator.hide()
                        }
                    }
                }
            }
        }
    }

    private fun handleVisibilityLayout(movie: Int, error: Int) {
        binding.apply {
            rvMovie.visibility = movie
            layoutError.viewError.visibility = error
        }
    }

    private fun handleErrorLayout(message: String, changeImage: Boolean = false) {
        binding.apply {
            layoutError.errorMessage.text = message
            if (changeImage) {
                layoutError.imgError.setImageResource(R.drawable.ic_empty_data)
                layoutError.btnRetry.visibility = View.GONE
            }
        }
    }


}