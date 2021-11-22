package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.api.POST_PER_PAGE
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.MoviePopularesDataSource
import com.perrofusion.peliseries.data.repository.MoviePopularesDataSourceFactory
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePopularListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MoviePopularesDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MoviePopularesDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MoviePopularesDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MoviePopularesDataSource::networkState)
    }
}