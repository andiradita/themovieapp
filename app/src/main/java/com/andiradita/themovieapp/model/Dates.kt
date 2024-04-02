package com.andiradita.themovieapp.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Dates(

	@field:SerializedName("maximum")
	val maximum: String? = null,

	@field:SerializedName("minimum")
	val minimum: String? = null
) : Parcelable