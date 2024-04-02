package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NowPlayingResponse(

    @field:SerializedName("dates")
    val dates: Dates? = null,

    @field:SerializedName("page")
    val page: Int? = 0,

    @field:SerializedName("total_pages")
    val totalPages: Int? = 0,

    @field:SerializedName("results")
    val results: List<MovieListResults>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = 0
) : Parcelable