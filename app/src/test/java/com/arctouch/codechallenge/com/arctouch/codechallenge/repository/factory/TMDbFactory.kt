package com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory

import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.PrimitiveFactory.randomInt
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.PrimitiveFactory.randomString
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse

object TMDbFactory {
    fun makeUpcomingMoviesResponse(totalPages: Int = 1, totalResults: Int = 10) = UpcomingMoviesResponse(
            randomInt(),
            makeMovieList(totalResults / totalPages),
            totalResults,
            totalPages
    )

    fun makeEmptyUpcomingMoviesResponse() = UpcomingMoviesResponse(0, makeMovieList(0), 0, 0)

    fun makeMovieList(number: Int = 3): List<Movie> {
        return mutableListOf<Movie>().apply {
            for (i in 1..number) this.add(makeMovie())
        }

    }

    private fun makeMovie(numberGenres: Int = 1): Movie {

        val genres = makeGenreList(numberGenres)
        val genresIds = genres.map { it.id }

        return Movie(
                randomInt(),
                randomString(),
                randomString(),
                genres,
                genresIds,
                randomString(),
                randomString(),
                randomString()
        )
    }

    private fun makeGenreList(number: Int = 1): List<Genre> {
        return mutableListOf<Genre>().apply {
            for (i in 1..number) {
                add(Genre(randomInt(), randomString()))
            }
        }
    }
}