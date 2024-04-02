package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieTrailerResponse(

    @field:SerializedName("id")
    val id: Int? = 0,

    @field:SerializedName("results")
    val results: List<TrailerResult>? = null
) : Parcelable