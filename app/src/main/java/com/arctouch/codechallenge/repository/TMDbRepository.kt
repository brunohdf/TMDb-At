package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class TMDbRepository(private val api: TmdbApi, private val cache: Cache) {

    fun getUpcomingMovies(page: Int = 1): Observable<List<Movie>> {

        val movies = api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }

        return Observable.zip(movies, getGenres(), BiFunction { t1, t2 ->
            t1.map { movie ->
                movie.copy(genres = t2.filter { movie.genreIds?.contains(it.id) ?: false })
            }
        })
    }

    private fun getGenres(): Observable<List<Genre>>? {
        return if (cache.genres.isNullOrEmpty()) {
            // this will run only once for first fetch
            api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        // save genres in cache to prevent new requests
                        cache.cacheGenres(it.genres)

                        it.genres
                    }
        } else Observable.just(cache.genres)
    }
}