package com.arctouch.codechallenge.com.arctouch.codechallenge.repository

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeEmptyUpcomingMoviesResponse
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeGenreList
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeGenreResponse
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory.makeUpcomingMoviesResponse
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.repository.TMDbRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class TMDbRepositoryTest {

    private lateinit var api: TmdbApi
    private lateinit var cache: Cache
    private lateinit var repository: TMDbRepository

    @Before
    fun setup() {
        api = mockk()
        cache = mockk()
        every { cache.genres } returns makeGenreList()
        every { cache.cacheGenres(any()) } just Runs
        repository = TMDbRepository(api, cache)

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

    @Test
    fun getUpcomingMovies_noWithCachedGenres_returnsGenresFromRemote() {
        var cachedGenres = makeGenreResponse()
        cachedGenres = cachedGenres.copy(genres = cachedGenres.genres.map { it.copy(name = GENRE_FROM_REMOTE) })

        every { api.upcomingMovies(any(), any(), any(), any()) } returns Observable.just(makeUpcomingMoviesResponse())
        every { api.genres(any(), any()) } returns Observable.just(cachedGenres)
        every { cache.genres } returns emptyList()

        repository.getUpcomingMovies()
                .test()
                .assertValue {
                    it.first().genres?.get(0)?.name == GENRE_FROM_REMOTE
                }
    }

    @Test
    fun getUpcomingMovies_noCachedGenres_returnsCachedGenres() {
        every { api.upcomingMovies(any(), any(), any(), any()) } returns Observable.just(makeUpcomingMoviesResponse())
        // remember that cached genres was mocked in @Before method

        repository.getUpcomingMovies()
                .test()
                .assertValue {
                    it.first().genres?.get(0)?.name != GENRE_FROM_REMOTE
                }
    }

    // a lot of scenarios could be tested here..

    companion object {
        const val GENRE_FROM_REMOTE = "GENRE FROM REMOTE"
    }
}