package com.arctouch.codechallenge.com.arctouch.codechallenge.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory.TMDbFactory
import com.arctouch.codechallenge.home.HomeViewModel
import com.arctouch.codechallenge.repository.TMDbRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    lateinit var repository: TMDbRepository
    lateinit var viewModel: HomeViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun destroy() {
        unmockkAll()
    }

    @Test
    fun getUpcomingMovies_noDataOnRepository_returnsEmpty() {
        // Given
        every { repository.getUpcomingMovies() } returns Observable.just(emptyList())

        // When
        viewModel.fetchUpcomingMovies()

        // then
        assertTrue(viewModel.upcomingMovies().value?.isEmpty() ?: false)
    }

    @Test
    fun getUpcomingMovies_withDataOnRepository_returnsData() {
        // Given
        every { repository.getUpcomingMovies() } returns Observable.just(TMDbFactory.makeMovieList())

        // When
        viewModel.fetchUpcomingMovies()

        // then
        assertEquals(3, viewModel.upcomingMovies().value?.size)
    }

    @Test
    fun getUpcomingMovies_withFailure_shouldNotReturnData() {
        // Given
        every { repository.getUpcomingMovies() } returns Observable.error(Throwable())

        // When
        viewModel.fetchUpcomingMovies()

        // then
        assertNull(viewModel.upcomingMovies().value)
    }

    // @Revisor I hope that i will have enough time to add more test cases later, for now i am prioritizing the main requirements
}