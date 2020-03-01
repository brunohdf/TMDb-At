package com.arctouch.codechallenge.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.detail.DetailActivity.Companion.MOVIE_ID
import com.arctouch.codechallenge.extension.formatToBrazillian
import com.arctouch.codechallenge.extension.loadRemoteImage
import com.arctouch.codechallenge.extension.visible
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MessageUtil
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import kotlinx.android.synthetic.main.detail_fragment.*
import org.koin.android.ext.android.inject
import java.util.*

class DetailFragment : Fragment() {

    var movieId: Long = 0

    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    companion object {
        fun newInstance(movieId: Long) = DetailFragment().apply {
            arguments = Bundle().apply {
                putLong(MOVIE_ID, movieId)
            }
        }
    }

    private val viewModel: DetailViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindEvents()

        movieId = arguments?.getLong(MOVIE_ID) ?: 0
        if (movieId > 0)
            viewModel.fetchDetails(movieId)
        else
            MessageUtil.displayError(context, R.string.invalid_parameter)
    }

    private fun bindEvents() {
        viewModel.details().observe(viewLifecycleOwner, Observer {
            bindDetail(it)
        })

        viewModel.showLoading().observe(viewLifecycleOwner, Observer {
            progressBar.visible(it)
        })
    }

    private fun bindDetail(data: Movie) {
        data.backdropPath?.let {
            backdropImage.loadRemoteImage(requireContext(), movieImageUrlBuilder.buildBackdropUrl(it))
        }

        data.posterPath?.let {
            posterImage.loadRemoteImage(requireContext(), movieImageUrlBuilder.buildPosterUrl(it))
        }

        title.text = data.title
        releaseDate.text = data.releaseDate?.formatToBrazillian()
        overview.text = if (data.overview.isNullOrBlank()) getString(R.string.no_overview) else data.overview
        data.genres?.let { genreList ->
            val inlineGenres = genreList.joinToString(", ") { it.name.toLowerCase(Locale.ROOT) }
            genres.text = resources.getQuantityString(R.plurals.genres, genreList.size, inlineGenres)
        }
    }
}
