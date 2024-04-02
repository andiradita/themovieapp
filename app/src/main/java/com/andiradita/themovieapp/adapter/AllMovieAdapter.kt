package com.andiradita.themovieapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.BuildConfig
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.model.MovieListResults
import com.andiradita.themovieapp.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AllMovieAdapter(private val type: String) :
    RecyclerView.Adapter<AllMovieAdapter.ViewHolder>() {
    private val data = ArrayList<MovieListResults>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(list: List<MovieListResults>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val tvOverview: TextView
        val tvReleasedDate: TextView
        val imgMovie: ImageView

        init {
            tvTitle = view.findViewById(R.id.tv_movie_title)
            tvOverview = view.findViewById(R.id.tv_movie_desc)
            tvReleasedDate = view.findViewById(R.id.tv_movie_release_date)
            imgMovie = view.findViewById(R.id.img_movie)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_rated_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(data[position]) {
                tvTitle.text = title
                tvOverview.text = overview
                if (type == Constants.TOP_RATED) tvReleasedDate.text =
                    "${Constants.LABEL_POPULARITY}$popularity"
                else tvReleasedDate.text = "${Constants.LABEL_RELEASED_AT}$releaseDate"

                Glide.with(imgMovie.context)
                    .load("${BuildConfig.PHOTO_BASE_URL}$posterPath")
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                    )
                    .into(imgMovie)
            }
        }
    }
}