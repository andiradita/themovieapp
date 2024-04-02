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
import com.andiradita.themovieapp.model.ReviewResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ReviewAdapter() :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ReviewResult>() {
        override fun areItemsTheSame(oldItem: ReviewResult, newItem: ReviewResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReviewResult, newItem: ReviewResult): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAuthor: TextView
        val tvContent: TextView
        val tvCreatedAt: TextView
        val imageAuthor: ImageView

        init {
            tvAuthor = view.findViewById(R.id.tv_author_name)
            tvContent = view.findViewById(R.id.tv_content)
            tvCreatedAt = view.findViewById(R.id.tv_created_at)
            imageAuthor = view.findViewById(R.id.img_author)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                tvAuthor.text = authorDetails?.name
                tvContent.text = content
                tvCreatedAt.text = createdAt
                Glide.with(imageAuthor.context)
                    .load("${BuildConfig.PHOTO_BASE_URL}${authorDetails?.avatarPath} ")
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                    )
                    .into(imageAuthor)
            }
        }
    }
}