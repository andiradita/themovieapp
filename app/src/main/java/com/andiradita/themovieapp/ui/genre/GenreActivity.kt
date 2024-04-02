package com.andiradita.themovieapp.ui.genre

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.MainActivity
import com.andiradita.themovieapp.adapter.GenreAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.GenreActivityBinding
import com.andiradita.themovieapp.listeners.OnGenreClickListener
import com.andiradita.themovieapp.model.GenresItem
import com.andiradita.themovieapp.utils.Constants
import com.andiradita.themovieapp.utils.PreferencesHelper

class GenreActivity : AppCompatActivity() {
    private lateinit var binding: GenreActivityBinding
    private var adapter: GenreAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val viewModel: GenreViewModel by viewModels()
    private var genreIds = ArrayList<Int>()
    private var allGenres = ""
    private var genreIdList = ArrayList<GenresItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GenreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = Constants.TITLE_MENU_GENRE

        setupRecyclerView()
        observeGenreMovie()
        allGenres = this@GenreActivity.let { PreferencesHelper.getGenreList(it) }.toString()
        binding.apply {
            tvContinue.setOnClickListener { view ->
                val result = StringBuilder()
                genreIds.forEachIndexed { index, genre ->
                    if (index > 0) {
                        result.append(",")
                    }
                    result.append(genre)
                }
                PreferencesHelper.saveGenreList(this@GenreActivity, result.toString());
                if (allGenres.isEmpty()) {
                    startActivity(Intent(view.context, MainActivity::class.java))
                } else {
                    setResult(1)
                }
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = GenreAdapter()
        adapter?.onGenreClickListener(object : OnGenreClickListener {
            override fun onClick(genreId: Int) {
                if (genreIds.contains(genreId)) genreIds.remove(genreId) else genreIds.add(genreId)
                if (genreIds.isEmpty()) binding.tvContinue.visibility =
                    View.GONE else binding.tvContinue.visibility =
                    View.VISIBLE
            }
        })
        layoutManager = GridLayoutManager(
            this, 2, GridLayoutManager.VERTICAL, false
        )
        binding.apply {
            rvGenre.adapter = adapter
            rvGenre.layoutManager = layoutManager
        }
    }

    private fun observeGenreMovie() {
        viewModel.getGenreMovies().observe(this) {
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
                        genreIdList.clear()
                        it.data?.let { genre -> genreIdList.addAll(genre) }
                        if (allGenres.isNotEmpty()) {
                            val arr = allGenres.split(",").toTypedArray()
                            arr.forEach { genreId ->
                                genreIds.add(genreId.toInt())
                                val data = genreIdList.find { it.id.toString() == (genreId) }
                                data?.isSelected = true
                            }
                        }
                        adapter?.setGenre(genreIdList)
                        adapter?.notifyDataSetChanged()
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }
}