package com.arctouch.codechallenge.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repository.TMDbRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.ResourceObserver

class DetailViewModel(private val repository: TMDbRepository) : ViewModel() {

    private var disposable: CompositeDisposable? = CompositeDisposable()

    private val details = MutableLiveData<Movie>()
    private val showLoading = MutableLiveData<Boolean>()

    fun details(): LiveData<Movie> = details
    fun showLoading(): LiveData<Boolean> = showLoading

    fun fetchDetails(movieId: Long) {
        val disposable = repository.getDetails(movieId)
                .subscribeWith(object : ResourceObserver<Movie>() {

                    override fun onNext(it: Movie) {
                        details.value = it
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
