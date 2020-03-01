package com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory

import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.PrimitiveFactory.randomInt
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.PrimitiveFactory.randomString
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse

// @Revisor: is important that the genre be know to compose genreIds and its equivalence at upcoming movies
// for test purposes, our list will have just one genre
private val genre = Genre(randomInt(), randomString())

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

    private fun makeMovie(): Movie {

        val genres = makeGenreList()
        val genresIds = genres.map { it.id }

        return Movie(
                randomInt().toLong(),
                randomString(),
                randomString(),
                genres,
                genresIds,
                randomString(),
                randomString(),
                randomString()
        )
    }

    fun makeGenreList(): List<Genre> {
        return listOf(genre)
    }

    fun makeGenreResponse() = GenreResponse(makeGenreList())
}