package com.jcorreia.currencyconverter.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jcorreia.currencyconverter.api.model.LatestRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jcorreia on 01/12/2017.
 *
 * Use Retrofit to get the JSON from the server and
 * LiveData to publish it to whoever is listening,
 * in this sample it's going to be the RatesViewModel
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

    /**
     * Use LiveData to subscribe the latest data from the server
     */
    fun getLatestRates() : LiveData<LatestRates> = latestRates

    fun refreshLatestRates(baseCurrency : String)  {

        service.getLatestRates(baseCurrency).enqueue(object : Callback<LatestRates> {
            override fun onFailure(call: Call<LatestRates>?, t: Throwable?) {
                //Handle errors - No internet connection, etc
            }

            override fun onResponse(call: Call<LatestRates>, response: Response<LatestRates>) {

                if (response.isSuccessful)
                    latestRates.value = response.body()
            }
        })
    }
}
