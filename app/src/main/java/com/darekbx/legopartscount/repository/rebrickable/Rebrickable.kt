package com.darekbx.legopartscount.repository.rebrickable

import com.darekbx.legopartscount.BuildConfig
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.darekbx.legopartscount.repository.rebrickable.model.RebrickableWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val REBRICKABLE_BASE_URL = "https://rebrickable.com/"

private val service: Rebrickable by lazy {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(BuildConfig.REBRICKABLE_API_KEY))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(REBRICKABLE_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    retrofit.create(Rebrickable::class.java)
}

fun getRebrickableService() = service

interface Rebrickable {

    @GET("api/v3/lego/parts")
    suspend fun searchParts(@Query("search") search: String): RebrickableWrapper<List<LegoPart>>
}

