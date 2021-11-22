package com.perrofusion.peliseries.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.data.value_object.Video
import com.perrofusion.peliseries.ui.movie.MainActivity
import com.perrofusion.peliseries.ui.movie.MainActivity.Companion.tagSelect
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TrailerDetailsNetworkDataSource (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedTrailerDetailsResponse =  MutableLiveData<List<Video>>()
    val downloadedTrailerResponse: MutableLiveData<List<Video>>
        get() = _downloadedTrailerDetailsResponse

    fun fetchTrailerDetails(Id: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            if(tagSelect){
                compositeDisposable.add(

                    apiService.getTvTrailerDetails(Id)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                _downloadedTrailerDetailsResponse.postValue(it.results)
                                _networkState.postValue(NetworkState.LOADED)
                            },
                            {
                                _networkState.postValue(NetworkState.ERROR)
                            }
                        )
                )
            }else{
                compositeDisposable.add(

                    apiService.getTrailerDetails(Id)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                _downloadedTrailerDetailsResponse.postValue(it.results)
                                _networkState.postValue(NetworkState.LOADED)
                            },
                            {
                                _networkState.postValue(NetworkState.ERROR)
                            }
                        )
                )
            }


        }

        catch (e: Exception){
        }


    }
}