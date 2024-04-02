package com.andiradita.themovieapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.listeners.OnGenreClickListener
import com.andiradita.themovieapp.model.GenresItem

class GenreAdapter() :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    private val data = ArrayList<GenresItem>()
    private lateinit var onGenreClickListener: OnGenreClickListener

    fun onGenreClickListener(onGenreClickListener: OnGenreClickListener) {
        this.onGenreClickListener = onGenreClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGenre(list: List<GenresItem>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGenre: TextView
        val layoutGenre: ConstraintLayout

        init {
            tvGenre = view.findViewById(R.id.tv_genre)
            layoutGenre = view.findViewById(R.id.layout_genre)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(data[position]) {
                tvGenre.text = name
                if (isSelected) {
                    layoutGenre.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.gold
                        )
                    )
                } else {
                    layoutGenre.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.white
                        )
                    )
                }
                itemView.setOnClickListener {
                    isSelected = !isSelected
                    onGenreClickListener.onClick(id ?: 0)
                    notifyDataSetChanged()
                }
            }
        }
    }
}