package com.perrofusion.peliseries.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.oxcoding.moviemvvm.ui.single_movie_details.*
import com.perrofusion.peliseries.R
import com.perrofusion.peliseries.data.api.POSTER_BASE_URL
import com.perrofusion.peliseries.data.api.TheMovieDBClient
import com.perrofusion.peliseries.data.api.TheMovieDBInterface
import com.perrofusion.peliseries.data.repository.NetworkState
import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.ui.movie.MainActivity.Companion.tagSelect
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var trailerViewModel: SingleTrailerViewModel
    private lateinit var tvViewModel: SingleTvViewModel

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var trailerRepository: TrailerDetailsRepository
    private lateinit var tvRepository: TvDetailsRepository

    var Manager: LinearLayoutManager? = null
    lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        trailerRepository = TrailerDetailsRepository(apiService)
        tvRepository = TvDetailsRepository(apiService)

        trailerViewModel = getTraileriewModel(movieId)
        viewModel = getViewModel(movieId)
        tvViewModel = getTvViewModel(movieId)
        adapter = VideoListAdapter(this)

       /* val gridLayoutManager = GridLayoutManager(this, 1)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
               return 1
            }
        };*/
        list_trailer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //list_trailer.layoutManager = gridLayoutManager
        list_trailer.setHasFixedSize(true)
        list_trailer.adapter = adapter









        trailerViewModel.trailerDetails.observe(this, Observer {
            adapter.submitList(it)
        })
        /* trailerViewModel.networkState.observe(this, Observer {
             progress_bar.visibility = if (trailerViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
             txt_error.visibility = if (trailerViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

             if (!trailerViewModel.listIsEmpty()) {
                 adapter.setNetworkState(it)
             }
         })*/
        (list_trailer.adapter as VideoListAdapter).notifyDataSetChanged()

        if(tagSelect){
            tvViewModel.tvDetails.observe(this, Observer {
                bindUI(it as MovieDetails)
            })

        }else{
            viewModel.movieDetails.observe(this, Observer {
                bindUI(it as MovieDetails)
            })
            viewModel.networkState.observe(this, Observer {
                progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
                txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

            })
        }







    }

    fun bindUI(it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        /*
        recyclerView_trailers.adapter = adapter*/




        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster);


    }


    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    private fun getTraileriewModel(Id: Int): SingleTrailerViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleTrailerViewModel(trailerRepository, Id) as T
            }
        })[SingleTrailerViewModel::class.java]
    }
    private fun getTvViewModel(Id: Int): SingleTvViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleTvViewModel(tvRepository, Id) as T
            }
        })[SingleTvViewModel::class.java]
    }

}
