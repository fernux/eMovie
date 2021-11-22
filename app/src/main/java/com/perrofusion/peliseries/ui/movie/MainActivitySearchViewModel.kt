package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivitySearchViewModel(private val movieRepository: SearchListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


     val searchPagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveSearchPagedList(compositeDisposable)
    }

     val  networkState : LiveData<NetworkState> by lazy {
         movieRepository.getNetworkState()
     }

    fun listIsEmpty(): Boolean {
        return searchPagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }




 }
