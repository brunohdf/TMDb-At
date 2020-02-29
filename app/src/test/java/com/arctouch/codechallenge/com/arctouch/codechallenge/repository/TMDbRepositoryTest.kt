package com.arctouch.codechallenge.com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeEmptyUpcomingMoviesResponse
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeUpcomingMoviesResponse
import com.arctouch.codechallenge.repository.TMDbRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class TMDbRepositoryTest {

    lateinit var api: TmdbApi
    lateinit var repository: TMDbRepository

    @Before
    fun setup() {
        api = mockk()
        repository = TMDbRepository(api)

        // specify which thread to use for IO and Main schedulers
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun getUpcomingMovies_withDataOnApi_returnsData() {
        every { api.upcomingMovies(any(), any(), any(), any()) } returns Observable.just(makeUpcomingMoviesResponse())

        repository.getUpcomingMovies()
                .test()
                .assertValue { it.isNotEmpty() }
    }

    @Test
    fun getUpcomingMovies_noDataOnApi_returnsEmpty() {
        every { api.upcomingMovies(any(), any(), any(), any()) } returns Observable.just(makeEmptyUpcomingMoviesResponse())

        repository.getUpcomingMovies()
                .test()
                .assertValue { it.isEmpty() }
    }
}