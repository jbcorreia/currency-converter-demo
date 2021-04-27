package com.jcorreia.currencyconverter.ui

import androidx.recyclerview.widget.DiffUtil
import com.jcorreia.currencyconverter.viewmodel.model.CurrencyRate

/**
 * Created by jcorreia on 05/12/2017.
 *
 * Calculate the difference between list of rates
 * The only payload we need is for when the value is updated
 * With Kotlin comparing items is easier since we declared
 * CurrencyRate as a data class
 */
class RatesDiff(private val oldList: List<CurrencyRate>, private val newList: List<CurrencyRate>) : DiffUtil.Callback() {

    companion object {
        const val VALUE_CHG = "VALUE_CHG"
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currency == newList[newItemPosition].currency
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val oldRate = oldList[oldItemPosition]
        val newRate = newList[newItemPosition]

        val payloadSet = mutableSetOf<String>()

        if (oldRate.value!=newRate.value)
            payloadSet.add(VALUE_CHG)

        return payloadSet
    }
}