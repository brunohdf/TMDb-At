package com.arctouch.codechallenge.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val showLoading = MutableLiveData<Boolean>()
    protected val displayError = MutableLiveData<Int>()

    fun showLoading(): LiveData<Boolean> = showLoading
    fun displayError(): LiveData<Int> = displayError

    protected var disposable: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }
}