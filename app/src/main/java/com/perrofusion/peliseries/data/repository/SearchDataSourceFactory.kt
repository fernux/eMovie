package com.perrofusion.peliseries.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable )
    : DataSource.Factory<Int, Movie>() {

    val searchLiveDataSource =  MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val searchDataSource = SearchDataSource(apiService,compositeDisposable)

        searchLiveDataSource.postValue(searchDataSource)
        return searchDataSource
    }
}