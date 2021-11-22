package com.perrofusion.peliseries.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class TvPopularesDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable )
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<TvPopularesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val tvDataSource = TvPopularesDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(tvDataSource)
        return tvDataSource
    }
}