package com.jcorreia.currencyconverter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.jcorreia.currencyconverter.api.ApiClient
import com.jcorreia.currencyconverter.api.ApiResult
import com.jcorreia.currencyconverter.api.model.LatestRates
import com.jcorreia.currencyconverter.viewmodel.model.CurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by jcorreia on 01/12/2017.
 */
class RatesViewModel : ViewModel() {

    private val RATE_UPDATE: Any = Object()

    // Default values
    private var baseCurrency: String = "EUR"
    private var baseValue: Float = 100F

    private val currencyRates: MutableLiveData<List<CurrencyRate>> = MutableLiveData()

    /**
     * Converts the latest data to a List<CurrencyRate>
     *
     * @param latestRates Latest Rates received from ApiClient.
     */
    private fun updateLatestRates(latestRates: LatestRates) {

        val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

        synchronized(RATE_UPDATE) {

            newCurrencyRates.add(CurrencyRate(latestRates.base!!, 1.0F, baseValue))

            latestRates.rates?.forEach { (currency, rate) ->
                newCurrencyRates.add(CurrencyRate(currency, rate, rate*baseValue))
            }

            currencyRates.postValue(newCurrencyRates)
        }
    }

    fun getCurrencyRates() : LiveData<List<CurrencyRate>> = currencyRates

    fun refreshRates() {
        viewModelScope.launch(Dispatchers.Default) {
            val apiResult = ApiClient.refreshLatestRates(baseCurrency);

            when (apiResult) {
                is ApiResult.Success -> updateLatestRates(apiResult.data)
                is ApiResult.UnknownError -> Log.d("RatesViewModel","ApiError: ${apiResult.errorMessage}")
            }
        }
    }

    fun setNewBase(newBaseCurrency: String, newBaseValue: Float) {

        // Ignore if same
        if (baseCurrency == newBaseCurrency)
            return;

        baseCurrency = newBaseCurrency
        baseValue = newBaseValue
        refreshRates()
    }

    fun setNewBaseValue(value: Float) {
        // Ignore so it doesn't enter an infinite loop
        if (baseValue.equals(value))
            return

        viewModelScope.launch(Dispatchers.Default) {
            synchronized(RATE_UPDATE) {
                baseValue = value

                val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

                currencyRates.value?.forEach { newCurrencyRates.add(CurrencyRate(it.currency,it.rate,it.rate*baseValue))}
                currencyRates.postValue(newCurrencyRates)
            }
        }
    }
}
