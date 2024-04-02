package com.andiradita.themovieapp.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.ReviewAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.ActivityDetailMovieBinding
import com.andiradita.themovieapp.model.DetailMovieResponse
import com.andiradita.themovieapp.ui.review.ReviewActivity
import com.andiradita.themovieapp.ui.trailer.TrailerActivity
import com.andiradita.themovieapp.utils.Constants
import com.bumptech.glide.request.RequestOptions

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding
    private val viewModel: DetailViewModel by viewModels()
    private var adapter: ReviewAdapter? = null
    private var layoutManager: LayoutManager? = null

    companion object {
        const val movieId = "movieId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = Constants.TITLE_MENU_DETAIL
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(movieId, 0)
        observerReviewMovie(id)
        observeDetailMovie(id)
        setupRecyclerView()

        binding.layoutError.btnRetry.setOnClickListener {
            observerReviewMovie(id)
            observeDetailMovie(id)
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
        adapter = ReviewAdapter()
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.apply {
            rvReview.adapter = adapter
            rvReview.layoutManager = layoutManager
        }
    }

    private fun observerReviewMovie(id: Int) {
        viewModel.getReviewMovie(id).observe(this) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        handleReviewData(View.GONE, View.GONE, View.VISIBLE)
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        binding.progressIndicator.hide()
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        it.data?.results.let { review ->
                            if (review != null) {
                                if (review.isEmpty()) {
                                    handleReviewData(View.GONE, View.GONE, View.VISIBLE)
                                } else {
                                    handleReviewData(View.VISIBLE, View.VISIBLE, View.GONE)
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

    private fun handleReviewData(recyclerview: Int, btnReview: Int, reviewMsg: Int) {
        binding.apply {
            rvReview.visibility = recyclerview
            tvReviewScreen.visibility = btnReview
            tvNoReview.visibility = reviewMsg
            tvNoReview.text = Constants.ERROR_MESSAGE_NO_REVIEWS
        }
    }

    private fun observeDetailMovie(id: Int) {
        viewModel.getDetailMovie(id).observe(this) {
            if (it != null) {
                binding.apply {
                    when (it) {
                        is LoadingState.Loading -> {
                            handleVisibilityLayout(View.GONE, View.GONE)
                            progressIndicator.show()
                        }

                        is LoadingState.Error -> {
                            handleVisibilityLayout(View.GONE, View.VISIBLE)
                            handleErrorLayout(it.errorMessage)
                            progressIndicator.hide()
                            Toast.makeText(this@DetailActivity, it.errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is LoadingState.Success -> {
                            handleVisibilityLayout(View.VISIBLE, View.GONE)
                            detailMapper(it.data)
                            progressIndicator.hide()
                        }
                    }
                }

            }
        }
    }

    private fun detailMapper(detailResponse: DetailMovieResponse) {
        with(detailResponse) {
            binding.apply {
                imagePlayTrailer.setOnClickListener {
                    startTrailerActivity(id!!, backdropPath)
                }
                btnCheckTrailer.setOnClickListener {
                    startTrailerActivity(id!!, backdropPath)
                }
                tvReviewScreen.setOnClickListener {
                    val intent =
                        Intent(
                            this@DetailActivity,
                            com.andiradita.themovieapp.ui.review.ReviewActivity::class.java
                        )
                    intent.putExtra(com.andiradita.themovieapp.ui.review.ReviewActivity.movieId, id)
                    startActivity(intent)
                }
                tvTitle.text = "${originalTitle} - ${originalLanguage?.uppercase()}"
                tvReleaseDate.text = "${status} - ${releaseDate}"
                tvOverview.text = overview
                com.bumptech.glide.Glide.with(this@DetailActivity)
                    .load("${com.andiradita.themovieapp.BuildConfig.PHOTO_BASE_URL}${backdropPath}")
                    .apply(
                        RequestOptions()
                            .placeholder(com.andiradita.themovieapp.R.drawable.no_image_available)
                            .error(com.andiradita.themovieapp.R.drawable.no_image_available)
                    )
                    .into(imgMovieDetail)

                com.bumptech.glide.Glide.with(this@DetailActivity)
                    .load("${com.andiradita.themovieapp.BuildConfig.PHOTO_BASE_URL}${posterPath}")
                    .apply(
                        RequestOptions()
                            .placeholder(com.andiradita.themovieapp.R.drawable.no_image_available)
                            .error(com.andiradita.themovieapp.R.drawable.no_image_available)
                    )
                    .into(imgPoster)
                val genreBuilder = StringBuilder()
                val companyBuilder = StringBuilder()
                val languageBuilder = StringBuilder()
                genres?.forEachIndexed { index, genre ->
                    if (index > 0) {
                        genreBuilder.append(", ")
                    }
                    genreBuilder.append(genre?.name)

                }
                tvGenre.text = genreBuilder
                productionCompanies?.forEachIndexed { index, company ->
                    if (index > 0) {
                        companyBuilder.append(", ")
                    }
                    companyBuilder.append(company?.name)

                }
                tvCompanyProd.text = companyBuilder
                spokenLanguages?.forEachIndexed { index, lang ->
                    if (index > 0) {
                        languageBuilder.append(", ")
                    }
                    languageBuilder.append(lang?.name)

                }
                tvSpokenLang.text = languageBuilder
                imagePlayTrailer.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun startTrailerActivity(id: Int, cover: String?) {
        val intent = Intent(this@DetailActivity, TrailerActivity::class.java)
        intent.putExtra(TrailerActivity.movieId, id)
        intent.putExtra(TrailerActivity.poster, cover)
        startActivity(intent)
    }

    private fun handleVisibilityLayout(detail: Int, error: Int) {
        binding.apply {
            svDetail.visibility = detail
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