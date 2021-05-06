package com.jcorreia.currencyconverter.api

import androidx.lifecycle.MutableLiveData
import com.jcorreia.currencyconverter.api.model.LatestRates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jcorreia on 01/12/2017.
 *
 * Use Retrofit to get the JSON from the server
 * using coroutines
 */
object ApiClient {

    private val BASE_URL: String = "https://revolut.duckdns.org/"

    private val service: ApiInterface

    private val latestRates: MutableLiveData<LatestRates> = MutableLiveData()

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(ApiInterface::class.java)
    }

    suspend fun refreshLatestRates(baseCurrency : String) : ApiResult<LatestRates> {

        try {
            val response = withContext(Dispatchers.IO) { service.getLatestRates(baseCurrency) }

            if (response.isSuccessful)
                return ApiResult.Success(response.body()!!)

            // Here we would need to parse the error properly just PoC
            return ApiResult.UnknownError("Unknown Error");

        } catch (e : Exception){
            return ApiResult.UnknownError("Exception ${e.message}");
        }

    }

}
