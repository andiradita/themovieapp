package com.andiradita.themovieapp.ui.toprated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andiradita.themovieapp.R
import com.andiradita.themovieapp.adapter.AllMovieAdapter
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.databinding.TopRatedFragmentBinding
import com.andiradita.themovieapp.utils.Constants

class TopRatedFragment : Fragment() {

    private var _binding: TopRatedFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: AllMovieAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val viewModel: TopRatedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val topRatedViewModel =
            ViewModelProvider(this).get(TopRatedViewModel::class.java)

        _binding = TopRatedFragmentBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observerTopRatedMovie()
        binding.layoutError.btnRetry.setOnClickListener {
            observerTopRatedMovie()
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = AllMovieAdapter(Constants.TOP_RATED)
        layoutManager = LinearLayoutManager(
            this@TopRatedFragment.context, LinearLayoutManager.VERTICAL, false
        )
        binding.apply {
            rvTopRated.adapter = adapter
            rvTopRated.layoutManager = layoutManager
        }
    }

    private fun observerTopRatedMovie() {
        viewModel.getTopRatedMovie().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is LoadingState.Loading -> {
                        handleVisibilityLayout(View.VISIBLE, View.GONE)
                        binding.progressIndicator.show()
                    }

                    is LoadingState.Error -> {
                        handleVisibilityLayout(View.GONE, View.VISIBLE)
                        handleErrorLayout(it.errorMessage)
                        binding.progressIndicator.hide()
                        Toast.makeText(
                            this@TopRatedFragment.context,
                            it.errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is LoadingState.Success -> {
                        it.data.results.let { data ->
                            if (data != null) {
                                if (data.isEmpty()) {
                                    handleVisibilityLayout(View.GONE, View.VISIBLE)
                                    handleErrorLayout("Data not available!", true)
                                } else {
                                    handleVisibilityLayout(View.VISIBLE, View.GONE)
                                    adapter?.setMovies(data)
                                }
                            }
                        }
                        binding.progressIndicator.hide()
                    }
                }
            }
        }
    }

    private fun handleVisibilityLayout(rv: Int, error: Int) {
        binding.apply {
            rvTopRated.visibility = rv
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}