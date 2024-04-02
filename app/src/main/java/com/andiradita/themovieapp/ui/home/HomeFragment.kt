package com.andiradita.themovieapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.andiradita.themovieapp.adapter.MovieListAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.HomeFragmentBinding
import com.andiradita.themovieapp.listeners.OnMovieClickListener
import com.andiradita.themovieapp.ui.details.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: MovieListAdapter? = null
    private var layoutManager: LayoutManager? = null
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getDiscoverMovie()
        setupRecyclerView()
        observeDiscoverMovie()
        return binding.root
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
                        viewModel.getDiscoverMovie()
                    }
                }
            })
        }
    }

    private fun observeDiscoverMovie() {
        viewModel.discoverMovieResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        binding.progressIndicator.hide()
                        Toast.makeText(this.context, it.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is LoadingState.Success -> {
                        it.data?.results?.let { mov -> adapter?.differ?.submitList(mov.toList()) }
                        binding.progressIndicator.hide()
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