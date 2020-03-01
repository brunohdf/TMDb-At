package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.extension.visible
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.EndlessScrollListener
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()

    private val moviesList = mutableListOf<Movie>()
    private val adapter = HomeAdapter(moviesList, onClickMovie())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        setupRecyclerView()

        bindEvents()
        viewModel.fetchUpcomingMovies()
    }

    private fun bindEvents() {
        viewModel.upcomingMovies().observe(this, Observer { movies ->
            val startPosition = moviesList.size + 1
            moviesList.addAll(movies)
            adapter.notifyItemRangeInserted(startPosition, movies.size)
        })

        viewModel.showLoading().observe(this, Observer { showLoading ->
            progressBar.visible(showLoading)
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
            it.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    viewModel.fetchUpcomingMovies(currentPage.toLong())
                }
            })
        }
    }

    private fun onClickMovie(): (Long) -> Unit {
        return { movieId: Long ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.MOVIE_ID, movieId)
            }.run {
                startActivity(this)
            }

        }
    }
}
