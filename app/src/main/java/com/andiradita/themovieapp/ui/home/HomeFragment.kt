package com.andiradita.themovieapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.MovieListAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.HomeFragmentBinding
import com.andiradita.themovieapp.listeners.OnMovieClickListener
import com.andiradita.themovieapp.model.MovieResult
import com.andiradita.themovieapp.ui.details.DetailActivity
import com.andiradita.themovieapp.ui.genre.GenreActivity
import com.andiradita.themovieapp.utils.PreferencesHelper

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: MovieListAdapter? = null
    private var layoutManager: LayoutManager? = null
    private val viewModel: HomeViewModel by viewModels()
    private var genreIds: String = ""
    private var movieList = ArrayList<MovieResult>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        genreIds = this@HomeFragment.context?.let { PreferencesHelper.getGenreList(it) }.toString()
        if (movieList.isEmpty()) viewModel.getDiscoverMovie(genreIds)
        setupRecyclerView()
        observeDiscoverMovie()

        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.getDiscoverMovie(genreIds)
        }
        return binding.root
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 1) {
                genreIds =
                    this@HomeFragment.context?.let { PreferencesHelper.getGenreList(it) }.toString()
                with(viewModel) {
                    page = 1
                    getDiscoverMovie(genreIds)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider())
    }

    private fun menuProvider() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.main_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_genre_settings -> {
                    val intent = Intent(this@HomeFragment.context, GenreActivity::class.java)
                    launcher.launch(intent)
                    true
                }

                else -> false
            }
        }
    }


    private fun setupRecyclerView() {
        adapter = MovieListAdapter()
        adapter?.onMovieClickListener(object : OnMovieClickListener {
            override fun onMovieDetailClick(movieId: Int) {
                val intent = Intent(this@HomeFragment.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.movieId, movieId)
                startActivity(intent)
            }
        })
        layoutManager = GridLayoutManager(
            this.context, 2, GridLayoutManager.VERTICAL, false
        )
        binding.apply {
            rvMovie.adapter = adapter
            rvMovie.layoutManager = layoutManager
            rvMovie.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!rvMovie.canScrollVertically(1)) {
                        viewModel.getDiscoverMovie(genreIds)
                    }
                }
            })
        }
    }

    private fun observeDiscoverMovie() {
        viewModel.discoverMovieResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.apply {
                    when (it) {
                        is LoadingState.Loading -> {
                            layoutError.viewError.visibility = View.GONE
                            progressIndicator.show()
                        }

                        is LoadingState.Error -> {
                            progressIndicator.hide()
                            rvMovie.visibility = View.GONE
                            layoutError.viewError.visibility = View.VISIBLE
                            layoutError.errorMessage.text = it.errorMessage
                            Toast.makeText(
                                this@HomeFragment.context,
                                it.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is LoadingState.Success -> {
                            movieList.clear()
                            it.data?.results?.let { mov -> movieList.addAll(mov.toList()) }
                            adapter?.differ?.submitList(movieList)
                            adapter?.notifyDataSetChanged()
                            layoutError.viewError.visibility = View.GONE
                            rvMovie.visibility = View.VISIBLE
                            progressIndicator.hide()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}