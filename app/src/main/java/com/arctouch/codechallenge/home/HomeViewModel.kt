package com.arctouch.codechallenge.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.TMDbRepository
import io.reactivex.observers.ResourceObserver

class HomeViewModel(private val repository: TMDbRepository) : BaseViewModel() {

    // @Revisor: the number of liveData attributes could be improved by using a ViewState pattern, it will be done if there is time left
    // for now I will prioritize the main requirements
    private val upcomingMovies = MutableLiveData<List<Movie>>()

    private lateinit var movies: List<Movie>

    fun upcomingMovies(): LiveData<List<Movie>> = upcomingMovies

    fun fetchUpcomingMovies(page: Long = 1) {
        showLoading.value = true

        if (::movies.isInitialized) {
            // it works  because ViewMode is lifecycle-aware, so it too easy to keep our data! :)
            upcomingMovies.value = movies
        } else {
            val observable = repository.getUpcomingMovies(page)
                    .subscribeWith(
                            object : ResourceObserver<List<Movie>>() {

                                override fun onNext(it: List<Movie>) {
                                    movies = it
                                    upcomingMovies.value = movies
                                }

                                override fun onComplete() {
                                    showLoading.value = false
                                }

                                override fun onError(e: Throwable) {
                                    displayError.value = R.string.error_message
                                    showLoading.value = false
                                }
                            }
                    )

            disposable?.add(observable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }
}