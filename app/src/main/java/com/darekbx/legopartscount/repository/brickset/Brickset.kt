package com.darekbx.legopartscount.repository.brickset

import com.darekbx.legopartscount.repository.brickset.model.GetSetsResult
import com.darekbx.legopartscount.repository.brickset.model.LoginResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private val BRICKSET_BASE_URL = "https://brickset.com/api/v3.asmx/"

private val service: Brickset by lazy {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BRICKSET_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(Brickset::class.java)
}

fun getBricketService() = service

interface Brickset {

    companion object {
        val DEFAULT_PAGE_SIZE = 1000
    }

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("apiKey") apiKey: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResult

    @FormUrlEncoded
    @POST("getSets")
    suspend fun getSets(
        @Field("apiKey") apiKey: String,
        @Field("userHash") userHash: String,
        @Field("params") params: String,
    ): GetSetsResult
}
