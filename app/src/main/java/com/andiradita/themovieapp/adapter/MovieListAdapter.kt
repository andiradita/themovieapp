package com.andiradita.themovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.BuildConfig
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.model.MovieResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieListAdapter() :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
//    private val data = ArrayList<MovieResult>()

//    @SuppressLint("NotifyDataSetChanged")
//    fun setMovies(list: List<MovieResult>) {
//        this.data.clear()
//        this.data.addAll(list)
//        notifyDataSetChanged()
//    }

    private val differCallback = object : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val tvDescription: TextView
        val image: ImageView

        init {
            tvTitle = view.findViewById(R.id.tv_movie_title)
            tvDescription = view.findViewById(R.id.tv_movie_desc)
            image = view.findViewById(R.id.img_movie)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                tvTitle.text = title
                tvDescription.text = overview
                Glide.with(image.context)
                    .load("${BuildConfig.PHOTO_BASE_URL}$posterPath ")
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                    )
                    .into(image)
            }
        }
    }
}