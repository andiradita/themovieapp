package com.andiradita.themovieapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailMovieResponse(

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @field:SerializedName("original_title")
    val originalTitle: String? = null,

    @field:SerializedName("imdb_id")
    val imdbId: String? = null,

    @field:SerializedName("runtime")
    val runtime: Int? = 0,

    @field:SerializedName("video")
    val video: Boolean? = false,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("revenue")
    val revenue: Int? = 0,

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem?>? = null,

    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @field:SerializedName("genres")
    val genres: List<GenresItem?>? = null,

    @field:SerializedName("popularity")
    val popularity: Float? = 0f,

    @field:SerializedName("vote_average")
    val voteAverage: Float? = 0f,

    @field:SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,

    @field:SerializedName("tagline")
    val tagline: String? = null,

    @field:SerializedName("id")
    val id: Int? = 0,

    @field:SerializedName("adult")
    val adult: Boolean? = false,

    @field:SerializedName("vote_count")
    val voteCount: Int? = 0,

    @field:SerializedName("budget")
    val budget: Int? = 0,

    @field:SerializedName("homepage")
    val homepage: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItem?>? = null

) : Parcelable