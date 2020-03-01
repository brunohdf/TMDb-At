package com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TMDbRepository(private val api: TmdbApi) {

    fun getUpcomingMovies(page: Int = 1): Observable<List<Movie>> {
        return api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }
    }
}