package com.andiradita.themovieapp.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.andiradita.themovieapp.BuildConfig
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.ReviewAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.ActivityDetailMovieBinding
import com.andiradita.themovieapp.ui.trailer.TrailerActivity
import com.bumptech.glide.Glide
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
        val id = intent.getIntExtra(movieId, 0)
        observerReviewMovie(id)
        observeDetailMovie(id)
        setupRecyclerView()
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
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        binding.progressIndicator.hide()
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        it.data?.results.let { review ->
                            if (review != null) {
                                adapter?.setReview(review)
                            }
                        }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }

    private fun observeDetailMovie(id: Int) {
        viewModel.getDetailMovie(id).observe(this) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        binding.layoutContent.visibility = View.INVISIBLE
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        binding.progressIndicator.hide()
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        with(it.data) {
                            binding.apply {
                                imagePlayTrailer.setOnClickListener {
                                    startTrailerActivity(id, backdropPath)
                                }
                                btnCheckTrailer.setOnClickListener {
                                    startTrailerActivity(id, backdropPath)
                                }
                                tvTitle.text = "${originalTitle} - ${originalLanguage?.uppercase()}"
                                tvReleaseDate.text = "${status} - ${releaseDate}"
                                tvOverview.text = overview
                                Glide.with(this@DetailActivity)
                                    .load("${BuildConfig.PHOTO_BASE_URL}${backdropPath}")
                                    .apply(
                                        RequestOptions()
                                            .placeholder(R.drawable.no_image_available)
                                            .error(R.drawable.no_image_available)
                                    )
                                    .into(imgMovieDetail)

                                Glide.with(this@DetailActivity)
                                    .load("${BuildConfig.PHOTO_BASE_URL}${posterPath}")
                                    .apply(
                                        RequestOptions()
                                            .placeholder(R.drawable.no_image_available)
                                            .error(R.drawable.no_image_available)
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
                                layoutContent.visibility = View.VISIBLE
                                imagePlayTrailer.visibility = View.VISIBLE
                            }
                        }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }

    private fun startTrailerActivity(id: Int, cover: String?) {
        val intent = Intent(this@DetailActivity, TrailerActivity::class.java)
        intent.putExtra(TrailerActivity.movieId, id)
        intent.putExtra(TrailerActivity.poster, cover)
        startActivity(intent)
    }
}