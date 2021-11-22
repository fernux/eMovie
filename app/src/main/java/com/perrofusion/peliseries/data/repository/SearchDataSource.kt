package com.perrofusion.peliseries.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.perrofusion.peliseries.data.api.FIRST_PAGE
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchDataSource (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Movie>(){

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        networkState.postValue(NetworkState.LOADING)

            compositeDisposable.add(

                apiService.getSearchMovie("spiderman")
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            callback.onResult(it.movieList, null, page+1)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            networkState.postValue(NetworkState.ERROR)
                            //Log.e("MovieDataSource", it.message)
                        }
                    )
            )




    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getSearchMovie("spiderman")
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if(it.totalPages >= params.key) {
                        callback.onResult(it.movieList, params.key+1)
                        networkState.postValue(NetworkState.LOADED)
                    }
                    else{
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                },
                {
                    networkState.postValue(NetworkState.ERROR)
                    //Log.e("MovieDataSource", it.message)
                }
            )


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}