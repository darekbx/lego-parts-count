package com.darekbx.legopartscount.repository.rebrickable

import com.darekbx.legopartscount.BuildConfig
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSetPart
import com.darekbx.legopartscount.repository.rebrickable.model.RebrickableWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
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
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(Rebrickable::class.java)
}

fun getRebrickableService() = service

interface Rebrickable {

    companion object {
        val DEFAULT_PAGE_SIZE = 1000
    }

    @GET("api/v3/lego/parts")
    suspend fun searchParts(
        @Query("search") search: String,
        @Query("page_size") pageSize: Int = DEFAULT_PAGE_SIZE
    ): RebrickableWrapper<List<LegoPart>>

    @GET("api/v3/lego/sets")
    suspend fun searchForSets(
        @Query("search") query: String,
        @Query("page_size") pageSize: Int = DEFAULT_PAGE_SIZE
    ): RebrickableWrapper<List<LegoSet>>

    @GET("api/v3/lego/sets/{set_num}")
    suspend fun fetchSet(
        @Path("set_num") setNumber: String,
        @Query("page_size") pageSize: Int = DEFAULT_PAGE_SIZE
    ): LegoSet

    @GET("api/v3/lego/sets/{set_num}/parts")
    suspend fun fetchSetParts(
        @Path("set_num") setNumber: String,
        @Query("page_size") pageSize: Int = DEFAULT_PAGE_SIZE
    ): RebrickableWrapper<List<LegoSetPart>>
}

