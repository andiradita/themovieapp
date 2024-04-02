package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopRatedResponse(

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,


    @field:SerializedName("results")
    val results: List<MovieListResults>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null
) : Parcelable