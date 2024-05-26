package com.k10tetry.xchange.feature.converter.data.remote

import com.k10tetry.xchange.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface XchangeCurrencyApi {

    @GET("/api/latest.json")
    suspend fun getRates(@Query("app_id") appId: String = BuildConfig.API_KEY): ResponseBody

    @GET("/api/currencies.json")
    suspend fun getCurrencies(@Query("app_id") appId: String = BuildConfig.API_KEY): ResponseBody

}