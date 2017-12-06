package com.jcorreia.currencyconverter.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jcorreia on 01/12/2017.
 */
class LatestRates {

    @SerializedName("base")
    @Expose
    val base: String? = null
    @SerializedName("date")
    @Expose
    val date: String? = null
    @SerializedName("rates")
    @Expose
    val rates: Map<String, Float>? = null
    
}