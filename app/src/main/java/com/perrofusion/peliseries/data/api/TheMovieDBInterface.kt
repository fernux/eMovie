package com.perrofusion.peliseries.data.api

import com.perrofusion.peliseries.data.value_object.MovieDetails
import com.perrofusion.peliseries.data.value_object.MovieResponse
import com.perrofusion.peliseries.data.value_object.VideoListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("tv/{tv_id}")
    fun getTvDetails(@Path("tv_id") id: Int): Single<MovieDetails>

    @GET("/search/movie")
    fun getSearchMovie(@Query("query") query:String): Single<MovieResponse>

    //@GET("/search/tv")
    //fun getSearchTV(@Path("query") query:String ,@Query("page") page: Int): Single<TVResponse>

    @GET("tv/popular")
    fun getPopularTv(@Query("page") page: Int): Single<MovieResponse>

    @GET("tv/top_rated")
    fun getTopRatedTv(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{tv_id}/videos")
    fun getTrailerDetails(@Path("tv_id") id: Int): Single<VideoListResponse>


    @GET("tv/{tv_id}/videos")
    fun getTvTrailerDetails(@Path("tv_id") id: Int): Single<VideoListResponse>

}