package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Movie
import com.perrofusion.peliseries.data.value_object.SearchResults
import com.perrofusion.peliseries.util.ApiException
import com.perrofusion.peliseries.util.NoInternetException
import com.perrofusion.peliseries.util.State
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val movieRepository : MoviePagedListRepository) : ViewModel() {

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
