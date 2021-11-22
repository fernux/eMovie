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

class SearchListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var searchPagedList: LiveData<PagedList<Movie>>
    lateinit var  searchDataSourceFactory: SearchDataSourceFactory

    fun fetchLiveSearchPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        searchDataSourceFactory = SearchDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        searchPagedList = LivePagedListBuilder(searchDataSourceFactory, config).build()

        return searchPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<SearchDataSource, NetworkState>(
            searchDataSourceFactory.searchLiveDataSource, SearchDataSource::networkState)
    }
}