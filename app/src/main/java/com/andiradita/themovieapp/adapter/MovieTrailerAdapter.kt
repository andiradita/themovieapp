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
import com.andiradita.themovieapp.listeners.OnTrailerClickListener
import com.andiradita.themovieapp.model.TrailerResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieTrailerAdapter() :
    RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder>() {
    private val data = ArrayList<TrailerResult>()
    private lateinit var cover: String

    private lateinit var onTrailerClickListener: OnTrailerClickListener
    fun onTrailerListener(onTrailerClickListener: OnTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(list: List<TrailerResult>, cover: String) {
        this.data.clear()
        this.data.addAll(list)
        this.cover = cover
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val imgMovie: ImageView

        init {
            tvTitle = view.findViewById(R.id.tv_movie_title)
            imgMovie = view.findViewById(R.id.img_movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trailer_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(data[position]) {
                tvTitle.text = name
                Glide.with(imgMovie.context)
                    .load("${BuildConfig.PHOTO_BASE_URL}$cover")
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                    )
                    .into(imgMovie)

                itemView.setOnClickListener {
                    key.let { onTrailerClickListener.onClick(it ?: "") }
                }
            }
        }
    }
}