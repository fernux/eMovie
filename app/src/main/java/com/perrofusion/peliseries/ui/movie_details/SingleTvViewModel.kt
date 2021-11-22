package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.data.value_object.Video
import io.reactivex.disposables.CompositeDisposable

class SingleTvViewModel (private val tvRepository : TvDetailsRepository, Id: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  tvDetails : LiveData<MovieDetails> by lazy {
        tvRepository.fetchSingleTvDetails(compositeDisposable,Id)
    }

    val networkState : LiveData<NetworkState> by lazy {
        tvRepository.getTrailerDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}