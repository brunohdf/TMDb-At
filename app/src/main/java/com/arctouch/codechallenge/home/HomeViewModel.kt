package com.arctouch.codechallenge.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.TMDbRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.ResourceObserver

class HomeViewModel(private val repository: TMDbRepository) : ViewModel() {

    private var disposable: CompositeDisposable? = CompositeDisposable()

    // @Revisor: the number of liveData attributes could be improved by using a ViewState pattern, it will be done if there is time left
    // for now I will prioritize the main requirements
    private val upcomingMovies = MutableLiveData<List<Movie>>()
    private val showLoading = MutableLiveData<Boolean>()

    private lateinit var movies: List<Movie>

    fun upcomingMovies(): LiveData<List<Movie>> = upcomingMovies
    fun showLoading(): LiveData<Boolean> = showLoading

    fun fetchUpcomingMovies(page: Long = 1) {
        showLoading.value = true

        if (::movies.isInitialized) {
            // it works  because ViewMode is lifecycle-aware, so it too easy to keep our data! :)
            upcomingMovies.value = movies
        } else {
            val disposable = repository.getUpcomingMovies(page)
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
                                    showLoading.value = false
                                }
                            }
                    )

            disposable.add(disposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }
}