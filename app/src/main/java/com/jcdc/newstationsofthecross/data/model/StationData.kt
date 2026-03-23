package com.jcdc.newstationsofthecross.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StationData(
    @SerializedName("title") val title: String,
    @SerializedName("introduction") val introduction: Introduction,
    @SerializedName("stations") val stations: List<Station>,
    @SerializedName("conclusion") val conclusion: Conclusion
) : Serializable

data class Introduction(
    @SerializedName("opening_song") val openingSong: Song?,
    @SerializedName("opening_prayer") val openingPrayer: String
) : Serializable

data class Song(
    @SerializedName("title") val title: String,
    @SerializedName("lyrics") val lyrics: String
) : Serializable

data class Station(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("common_response") val commonResponse: String,
    @SerializedName("scripture") val scripture: Scripture,
    @SerializedName("reflection") val reflection: String,
    @SerializedName("prayer") val prayer: String
) : Serializable

data class Scripture(
    @SerializedName("reference") val reference: String,
    @SerializedName("text") val text: String
) : Serializable

data class Conclusion(
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String
) : Serializable
