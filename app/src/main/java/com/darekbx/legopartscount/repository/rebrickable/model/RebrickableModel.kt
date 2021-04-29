package com.darekbx.legopartscount.repository.rebrickable.model

import com.google.gson.annotations.SerializedName

data class RebrickableWrapper<out T>(
    val count: Int,
    val results: T
)

data class LegoSet(
    @SerializedName("num_set")
    val setNumber: String,
    val name: String,
    val year: Int,
    @SerializedName("num_parts")
    val partsCount: Int,
    @SerializedName("set_img_url")
    val setImageUrl: String
)

data class LegoPart(
    @SerializedName("part_num")
    val partNumber: String,
    val name: String,
    @SerializedName("part_img_url")
    val partImageUrl: String
)
