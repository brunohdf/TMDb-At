package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.extension.visible
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()

    private val moviesList = mutableListOf<Movie>()
    private val adapter = HomeAdapter(moviesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        recyclerView.adapter = adapter

        bindEvents()
        viewModel.fetchUpcomingMovies()
    }

    private fun bindEvents() {
        viewModel.upcomingMovies().observe(this, Observer { movies ->
            moviesList.addAll(movies)
            adapter.notifyDataSetChanged()

            progressBar.visibility = View.GONE
        })

        viewModel.showLoading().observe(this, Observer { showLoading ->
            progressBar.visible(showLoading)
        })
    }
}
