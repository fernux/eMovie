package com.perrofusion.peliseries.data.value_object

import com.google.gson.annotations.SerializedName

data class MovieResponse(
        val page: Int,
        @SerializedName("results")
        val movieList: List<Movie>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)