package com.jcorreia.currencyconverter.api

import com.jcorreia.currencyconverter.api.model.LatestRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jcorreia on 01/12/2017.
 */
interface ApiInterface {

    @GET("latest")
    suspend fun getLatestRates(@Query("base") baseRate: String): Response<LatestRates>

}