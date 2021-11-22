package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityTvRatViewModel(private val movieRepository: TvRatListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


     val tvPagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

     val  networkState : LiveData<NetworkState> by lazy {
         movieRepository.getNetworkState()
     }

    fun listIsEmpty(): Boolean {
        return tvPagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }




 }
