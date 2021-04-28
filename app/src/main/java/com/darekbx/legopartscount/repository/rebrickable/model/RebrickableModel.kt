package com.darekbx.legopartscount.repository.rebrickable.model

import com.squareup.moshi.Json

data class RebrickableWrapper<out T>(
    val count: Int,
    val results: T
)

data class LegoSet(
    @field:Json(name = "num_set")
    val setNumber: String,
    val name: String,
    val year: Int,
    @field:Json(name = "num_parts")
    val partsCount: Int,
    @field:Json(name = "set_img_url")
    val setImageUrl: String
)

data class LegoPart(
    @field:Json(name = "part_num")
    val partNumber: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "part_img_url")
    val partImageUrl: String
)
