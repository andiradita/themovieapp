package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResponse(

    @field:SerializedName("id")
    val id: Int? = 0,

    @field:SerializedName("page")
    val page: Int? = 0,

    @field:SerializedName("total_pages")
    val totalPages: Int? = 0,

    @field:SerializedName("results")
    val results: List<ReviewResult>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = 0
) : Parcelable