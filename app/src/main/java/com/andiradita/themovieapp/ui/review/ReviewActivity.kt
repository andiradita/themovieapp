package com.andiradita.themovieapp.ui.review

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.ReviewAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.ActivityReviewBinding
import com.andiradita.themovieapp.utils.Constants

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private val viewModel: ReviewViewModel by viewModels()
    private var adapter: ReviewAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    companion object {
        const val movieId = "movieId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = Constants.TITLE_MENU_REVIEW
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(movieId, 0)
        setupRecyclerView(id)
        observerReviewMovie()
        viewModel.getReviewMovie(id)
        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.getReviewMovie(id)
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

    private fun setupRecyclerView(id: Int) {
        adapter = ReviewAdapter()
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.apply {
            rvReview.adapter = adapter
            rvReview.layoutManager = layoutManager
            rvReview.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!rvReview.canScrollVertically(1)) {
                        viewModel.getReviewMovie(id)
                    }
                }
            })
        }
    }

    private fun observerReviewMovie() {
        viewModel.movieReviewResponse.observe(this) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        handleVisibilityLayout(View.VISIBLE, View.GONE)
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        handleVisibilityLayout(View.GONE, View.VISIBLE)
                        handleErrorLayout(it.errorMessage)
                        binding.progressIndicator.hide()
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        it.data?.results.let { review ->
                            if (review != null) {
                                if (review.isEmpty()) {
                                    handleVisibilityLayout(View.GONE, View.VISIBLE)
                                    handleErrorLayout("Data not available!", true)
                                } else {
                                    handleVisibilityLayout(View.VISIBLE, View.GONE)
                                    adapter?.differ?.submitList(review.toList())
                                }
                            }
                        }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }

    private fun handleVisibilityLayout(review: Int, error: Int) {
        binding.apply {
            rvReview.visibility = review
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