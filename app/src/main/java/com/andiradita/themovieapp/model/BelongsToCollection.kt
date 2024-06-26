package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BelongsToCollection(

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = 0,

    @field:SerializedName("poster_path")
    val posterPath: String? = null
) : Parcelable