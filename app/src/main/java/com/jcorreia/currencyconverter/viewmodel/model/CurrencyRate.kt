package com.jcorreia.currencyconverter.viewmodel.model

/**
 * Created by jcorreia on 01/12/2017.
 *
 * Declare as Data Class so it reduces boilerplate
 * when comparing items
 */
data class CurrencyRate (
    val currency: String,
    val rate: Float,
    var value : Float
)