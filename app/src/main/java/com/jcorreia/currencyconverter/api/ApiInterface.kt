package com.jcorreia.currencyconverter.api

import com.jcorreia.currencyconverter.api.model.LatestRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jcorreia on 01/12/2017.
 */
interface ApiInterface {

    @GET("latest")
    fun getLatestRates(@Query("base") baseRate: String): Call<LatestRates>

}