package com.perrofusion.peliseries.ui.movie


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.perrofusion.peliseries.R
import com.perrofusion.peliseries.data.api.TheMovieDBClient
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.util.dismissKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_movie.*

class MainActivity : AppCompatActivity() {

    companion object {
        var tagSelect:Boolean = false
    }

    lateinit var radioGroup: RadioGroup
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewPopModel: MainActivityPopViewModel
    private lateinit var viewPopTvModel:MainActivityTvViewModel
    private lateinit var viewRatTvModel:MainActivityTvRatViewModel
    private lateinit var viewSearchModel:MainActivitySearchViewModel

    lateinit var movieRepository: MoviePagedListRepository
    lateinit var moviePopRepository: MoviePopularListRepository
    lateinit var tvPopRepository: TvPopularListRepository
    lateinit var tvRatRepository: TvRatListRepository
    lateinit var searchRepository: SearchListRepository


    lateinit var movieAdapter:PopularMoviePagedListAdapter
    private lateinit var searchView: SearchView
    var statusFilter:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        radioGroup = findViewById(R.id.radioGroup)
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)
        moviePopRepository= MoviePopularListRepository(apiService)
        tvPopRepository= TvPopularListRepository(apiService)
        tvRatRepository= TvRatListRepository(apiService)
        searchRepository= SearchListRepository(apiService)

        viewModel = getViewModel()
        viewPopModel= getViewModelPop()
        viewPopTvModel= getViewModelPopTv()
        viewRatTvModel=getViewModelRatTv()
        viewSearchModel=getViewModelSearch()


         movieAdapter = PopularMoviePagedListAdapter(this)


        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1
                else return 3
            }
        };

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter





        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })


        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })



        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.top_rated -> {
                    statusFilter=false
                    if(tagSelect){
                        viewRatTvModel = getViewModelRatTv()
                        viewRatTvModel.tvPagedList.observe(this, Observer {
                            movieAdapter.submitList(it)
                        })
                    }else{
                        viewModel = getViewModel()
                        viewModel.moviePagedList.observe(this, Observer {
                            movieAdapter.submitList(it)
                        })
                    }

                    //Toast.makeText(this,"Changed Play Now", Toast.LENGTH_SHORT).show()
                }
                R.id.most_popular -> {
                    statusFilter=true
                    if(tagSelect){
                        viewPopTvModel = getViewModelPopTv()
                        viewPopTvModel.tvPagedList.observe(this, Observer {
                            movieAdapter.submitList(it)
                        })

                    }else {
                        viewPopModel = getViewModelPop()
                        viewPopModel.moviePagedList.observe(this, Observer {
                            movieAdapter.submitList(it)

                        })
                    }

                    //Toast.makeText(this,"Changed Most Popular", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
            (rv_movie_list.adapter as PopularMoviePagedListAdapter).notifyDataSetChanged()
        })


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        searchView = menu.findItem(R.id.action_three).actionView as SearchView

        search(searchView)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_one -> {
                //newGame()
                tagSelect=false
                if(statusFilter){
                    viewPopModel = getViewModelPop()
                    viewPopModel.moviePagedList.observe(this, Observer {
                        movieAdapter.submitList(it)
                    })

                }else{
                    viewModel = getViewModel()
                    viewModel.moviePagedList.observe(this, Observer {
                        movieAdapter.submitList(it)
                    })
                }
                true
            }
            R.id.action_two -> {
                //showHelp()
                tagSelect=true
                if(statusFilter){
                    viewPopTvModel = getViewModelPopTv()
                    viewPopTvModel.tvPagedList.observe(this, Observer {
                        movieAdapter.submitList(it)
                    })

                }else{
                    viewRatTvModel = getViewModelRatTv()
                    viewRatTvModel.tvPagedList.observe(this, Observer {
                        movieAdapter.submitList(it)
                    })
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                dismissKeyboard(searchView)
                searchView.clearFocus()
                //viewModel.searchMovie(query)

                (rv_movie_list.adapter as PopularMoviePagedListAdapter).notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }


    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

    private fun getViewModelPop(): MainActivityPopViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityPopViewModel(moviePopRepository) as T
            }
        })[MainActivityPopViewModel::class.java]
    }

    private fun getViewModelPopTv(): MainActivityTvViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityTvViewModel(tvPopRepository) as T
            }
        })[MainActivityTvViewModel::class.java]
    }

    private fun getViewModelRatTv(): MainActivityTvRatViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityTvRatViewModel(tvRatRepository) as T
            }
        })[MainActivityTvRatViewModel::class.java]
    }
    private fun getViewModelSearch(): MainActivitySearchViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivitySearchViewModel(searchRepository) as T
            }
        })[MainActivitySearchViewModel::class.java]
    }

}