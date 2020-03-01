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

    fun upcomingMovies(): LiveData<List<Movie>> = upcomingMovies
    fun showLoading(): LiveData<Boolean> = showLoading

    fun fetchUpcomingMovies(page: Int = 1) {
        showLoading.value = true

        val disposable = repository.getUpcomingMovies(page)
                .subscribeWith(
                        object : ResourceObserver<List<Movie>>() {

                            override fun onNext(it: List<Movie>) {
                                upcomingMovies.value = it
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

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }
}