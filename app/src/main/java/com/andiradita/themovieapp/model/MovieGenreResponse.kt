package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieGenreResponse(

    @field:SerializedName("genres")
    val genres: List<GenresItem>? = null
) : Parcelable