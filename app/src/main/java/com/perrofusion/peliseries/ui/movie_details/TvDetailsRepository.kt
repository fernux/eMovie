package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.MovieDetailsNetworkDataSource
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.repository.TrailerDetailsNetworkDataSource
import com.perrofusion.peliseries.data.repository.TvDetailsNetworkDataSource
import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.data.value_object.Video

import io.reactivex.disposables.CompositeDisposable

class TvDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var tvDetailsNetworkDataSource: TvDetailsNetworkDataSource

    fun fetchSingleTvDetails (compositeDisposable: CompositeDisposable, Id: Int) : LiveData<MovieDetails> {

        tvDetailsNetworkDataSource = TvDetailsNetworkDataSource(apiService,compositeDisposable)
        tvDetailsNetworkDataSource.fetchTvDetails(Id)

        return tvDetailsNetworkDataSource.downloadedTvResponse

    }

    fun getTrailerDetailsNetworkState(): LiveData<NetworkState> {
        return tvDetailsNetworkDataSource.networkState
    }



}