package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Video
import io.reactivex.disposables.CompositeDisposable

class SingleTrailerViewModel (private val trailerRepository : TrailerDetailsRepository, Id: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  trailerDetails : LiveData<List<Video>> by lazy {
        trailerRepository.fetchSingleTrailerDetails(compositeDisposable,Id)
    }

    val networkState : LiveData<NetworkState> by lazy {
        trailerRepository.getTrailerDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
    fun listIsEmpty(): Boolean {
        return trailerDetails.value?.isEmpty() ?: true
    }


}