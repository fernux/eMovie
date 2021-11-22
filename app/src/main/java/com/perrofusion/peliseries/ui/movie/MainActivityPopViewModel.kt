package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityPopViewModel(private val movieRepository: MoviePopularListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


     val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

     val  networkState : LiveData<NetworkState> by lazy {
         movieRepository.getNetworkState()
     }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }




 }
