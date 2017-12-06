package com.jcorreia.currencyconverter.ui

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.jcorreia.currencyconverter.R
import com.jcorreia.currencyconverter.viewmodel.model.CurrencyRate

/**
 * Created by jcorreia on 05/12/2017.
 * Simple ViewHolder for the items in the RatesAdapter
 */
class RateViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val rateLayout: View
    val currencyName: TextView
    val currencyImage: ImageView
    val currencyDesc: TextView
    val currencyRateEdit: EditText

    init {
        rateLayout = this.itemView.findViewById(R.id.rateLayout)
        currencyImage = this.itemView.findViewById(R.id.currencyImageView)
        currencyName = this.itemView.findViewById(R.id.currencyNameTextView)
        currencyDesc = this.itemView.findViewById(R.id.currencyDescTextView)
        currencyRateEdit = this.itemView.findViewById(R.id.rateEditText)
    }

    fun bindTo(currencyRate: CurrencyRate, position: Int, valueWatcher: TextWatcher) {

        currencyRateEdit.isEnabled = position == 0

        currencyRateEdit.removeTextChangedListener(valueWatcher)
        // set on text changed listener
        if (position==0)
            currencyRateEdit.addTextChangedListener(valueWatcher)

        updateValue(currencyRate,position)
        updateCurrency(currencyRate,position)
    }

    fun updateValue(currencyRate: CurrencyRate, position: Int) {

        val newValue: String = "%.2f".format(currencyRate.value)

        if (currencyRateEdit.text.toString() != newValue) {
            currencyRateEdit.setText(newValue)
        }
    }

    private fun updateCurrency(currencyRate: CurrencyRate, position: Int) {

        // Update Currency and Flag
        val updatedCurrency = currencyRate.currency

        currencyName.text = updatedCurrency
        currencyDesc.text = getCurrencyDesc(updatedCurrency)

        val flagDrawable = ResourcesCompat.getDrawable(currencyImage.resources, getCurrencyFlag(updatedCurrency), null)
        currencyImage.setImageDrawable(flagDrawable)

    }

    private fun getCurrencyFlag(currencyName: String): Int {

        return when (currencyName) {
            "EUR" -> R.drawable.ic_eu
            "AUD" -> R.drawable.ic_au
            "BGN" -> R.drawable.ic_bg
            "BRL" -> R.drawable.ic_br
            "CAD" -> R.drawable.ic_ca
            "CHF" -> R.drawable.ic_ch
            "CNY" -> R.drawable.ic_cn
            "CZK" -> R.drawable.ic_cz
            "DKK" -> R.drawable.ic_dk
            "GBP" -> R.drawable.ic_gb
            "HKD" -> R.drawable.ic_hk
            "HRK" -> R.drawable.ic_hr
            "HUF" -> R.drawable.ic_hu
            "IDR" -> R.drawable.ic_id
            "ILS" -> R.drawable.ic_il
            "INR" -> R.drawable.ic_in
            "JPY" -> R.drawable.ic_jp
            "KRW" -> R.drawable.ic_kr
            "MXN" -> R.drawable.ic_mx
            "MYR" -> R.drawable.ic_my
            "NOK" -> R.drawable.ic_no
            "NZD" -> R.drawable.ic_nz
            "PHP" -> R.drawable.ic_ph
            "PLN" -> R.drawable.ic_pl
            "RON" -> R.drawable.ic_ro
            "RUB" -> R.drawable.ic_ru
            "SEK" -> R.drawable.ic_se
            "SGD" -> R.drawable.ic_sg
            "THB" -> R.drawable.ic_th
            "TRY" -> R.drawable.ic_tr
            "USD" -> R.drawable.ic_us
            "ZAR" -> R.drawable.ic_za

            else -> R.drawable.ic_eu
        }
    }

    private fun getCurrencyDesc(currencyName: String): String {

        return when (currencyName) {
            "EUR" -> currencyDesc.context.getString(R.string.EUR)
            "AUD" -> currencyDesc.context.getString(R.string.AUD)
            "BGN" -> currencyDesc.context.getString(R.string.BGN)
            "BRL" -> currencyDesc.context.getString(R.string.BRL)
            "CAD" -> currencyDesc.context.getString(R.string.CAD)
            "CHF" -> currencyDesc.context.getString(R.string.CHF)
            "CNY" -> currencyDesc.context.getString(R.string.CNY)
            "CZK" -> currencyDesc.context.getString(R.string.CZK)
            "DKK" -> currencyDesc.context.getString(R.string.DKK)
            "GBP" -> currencyDesc.context.getString(R.string.GBP)
            "HKD" -> currencyDesc.context.getString(R.string.HKD)
            "HRK" -> currencyDesc.context.getString(R.string.HRK)
            "HUF" -> currencyDesc.context.getString(R.string.HUF)
            "IDR" -> currencyDesc.context.getString(R.string.IDR)
            "ILS" -> currencyDesc.context.getString(R.string.ILS)
            "INR" -> currencyDesc.context.getString(R.string.INR)
            "JPY" -> currencyDesc.context.getString(R.string.JPY)
            "KRW" -> currencyDesc.context.getString(R.string.KRW)
            "MXN" -> currencyDesc.context.getString(R.string.MXN)
            "MYR" -> currencyDesc.context.getString(R.string.MYR)
            "NOK" -> currencyDesc.context.getString(R.string.NOK)
            "NZD" -> currencyDesc.context.getString(R.string.NZD)
            "PHP" -> currencyDesc.context.getString(R.string.PHP)
            "PLN" -> currencyDesc.context.getString(R.string.PLN)
            "RON" -> currencyDesc.context.getString(R.string.RON)
            "RUB" -> currencyDesc.context.getString(R.string.RUB)
            "SEK" -> currencyDesc.context.getString(R.string.SEK)
            "SGD" -> currencyDesc.context.getString(R.string.SGD)
            "THB" -> currencyDesc.context.getString(R.string.THB)
            "TRY" -> currencyDesc.context.getString(R.string.TRY)
            "USD" -> currencyDesc.context.getString(R.string.USD)
            "ZAR" -> currencyDesc.context.getString(R.string.ZAR)

            else -> "N/A"
        }
    }
}