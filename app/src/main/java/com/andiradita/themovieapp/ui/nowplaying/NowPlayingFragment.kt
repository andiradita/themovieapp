package com.andiradita.themovieapp.ui.nowplaying

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
import com.andiradita.themovieapp.databinding.NowPlayingFragmentBinding
import com.andiradita.themovieapp.utils.Constants

class NowPlayingFragment : Fragment() {

    private var _binding: NowPlayingFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: AllMovieAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val viewModel: NowPlayingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nowPlayingViewModel =
            ViewModelProvider(this).get(NowPlayingViewModel::class.java)

        _binding = NowPlayingFragmentBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observerNowPlayingMovie()
        binding.layoutError.btnRetry.setOnClickListener {
            observerNowPlayingMovie()
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = AllMovieAdapter(Constants.NOW_PLAYING)
        layoutManager = LinearLayoutManager(
            this@NowPlayingFragment.context, LinearLayoutManager.VERTICAL, false
        )
        binding.apply {
            rvNowPlaying.adapter = adapter
            rvNowPlaying.layoutManager = layoutManager
        }
    }

    private fun observerNowPlayingMovie() {
        viewModel.getNowPlayingMovie().observe(this) {
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
                            this@NowPlayingFragment.context,
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
            rvNowPlaying.visibility = rv
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