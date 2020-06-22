package com.arctouch.codechallenge.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.TMDbRepository
import io.reactivex.observers.ResourceObserver

class DetailViewModel(private val repository: TMDbRepository) : BaseViewModel() {

    private val details = MutableLiveData<Movie>()

    lateinit var movie: Movie

    fun details(): LiveData<Movie> = details

    fun fetchDetails(movieId: Long) {
        if (::movie.isInitialized) {
            // it works because ViewMode is lifecycle-aware, so it too easy to keep our data! :)
            details.value = movie
        } else {
            val observable = repository.getDetails(movieId)
                .subscribeWith(object : ResourceObserver<Movie>() {
                    override fun onNext(it: Movie) {
                        movie = it
                        details.value = movie
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
}
