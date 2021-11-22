package com.perrofusion.peliseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.perrofusion.peliseries.data.api.POST_PER_PAGE
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.*
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class TvPopularListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var tvPagedList: LiveData<PagedList<Movie>>
    lateinit var tvDataSourceFactory: TvPopularesDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        tvDataSourceFactory = TvPopularesDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        tvPagedList = LivePagedListBuilder(tvDataSourceFactory, config).build()

        return tvPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TvPopularesDataSource, NetworkState>(
            tvDataSourceFactory.moviesLiveDataSource, TvPopularesDataSource::networkState)
    }
}