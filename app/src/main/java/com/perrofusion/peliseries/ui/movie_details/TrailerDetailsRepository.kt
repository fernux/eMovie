package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.MovieDetailsNetworkDataSource
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.repository.TrailerDetailsNetworkDataSource
import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.data.value_object.Video

import io.reactivex.disposables.CompositeDisposable

class TrailerDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var trailerDetailsNetworkDataSource: TrailerDetailsNetworkDataSource

    fun fetchSingleTrailerDetails (compositeDisposable: CompositeDisposable, Id: Int) : MutableLiveData<List<Video>> {

        trailerDetailsNetworkDataSource = TrailerDetailsNetworkDataSource(apiService,compositeDisposable)
        trailerDetailsNetworkDataSource.fetchTrailerDetails(Id)

        return trailerDetailsNetworkDataSource.downloadedTrailerResponse

    }

    fun getTrailerDetailsNetworkState(): LiveData<NetworkState> {
        return trailerDetailsNetworkDataSource.networkState
    }



}