package com.jcorreia.currencyconverter.ui

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcorreia.currencyconverter.viewmodel.model.CurrencyRate
import com.jcorreia.currencyconverter.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * RatesAdapter to be used by the RecyclerView
 * It uses a DiffUtil Callback with payload to
 * minimize UI lag and unnecessary data reload
 */
class RatesAdapter(private val callback: OnRateInteraction) : RecyclerView.Adapter<RateViewHolder>() {

    private val valueWatcher: TextWatcher
    private var ratesList: List<CurrencyRate>? = null


    companion object {
        const val ROOT_RATE = 0
        const val OTHER_RATE = 1
    }

    interface OnRateInteraction {
        fun onRateChanged(currencyName: String, value: Float)
        fun onValueChanged(value: Float)
        fun scrollToTop()
    }

    init {

        /** We set out TextWatcher here so it can be reused
         *  This will pick up the value being entered in the root rate only
         */
        this.valueWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(newValue: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(newValue: Editable?) {
                val strValue: String = newValue.toString().trim()

                val value: Float = try {
                    strValue.toFloat()
                } catch (e: Exception) {
                    0F
                }

                ratesList!![0].value = value
                callback.onValueChanged(value)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {

        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.rate, parent, false)

        val rateHolder = RateViewHolder(view)

        rateHolder.rateLayout.setOnClickListener( object : View.OnClickListener  {
            override fun onClick(clickedView: View?) {

                val pos: Int = rateHolder.bindingAdapterPosition;
                if (pos != RecyclerView.NO_POSITION && pos>0) {
                    callback.onRateChanged(ratesList!![pos].currency, ratesList!![pos].value)
                }
            }
        });

        return rateHolder
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bindTo(ratesList!![position],position,valueWatcher)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int, payloads: MutableList<Any>) {

        val set = payloads.firstOrNull() as Set<*>?

        if (set==null || set.isEmpty() ) {
            return super.onBindViewHolder(holder, position, payloads)
        }

        if (set.contains(RatesDiff.VALUE_CHG)){
            holder.updateValue(ratesList!![position],position)
        }
    }

    override fun getItemCount(): Int {
        return if (ratesList==null) 0 else ratesList!!.size
    }

    /** Return two different types: Root and all others
     *  This makes sure that the user can only change
     *  the rool element's value
     */
    override fun getItemViewType(position: Int): Int {
        return if (position==0) ROOT_RATE else OTHER_RATE
    }

    /** Since the data is coming from Network on a loop
     *  we try to update always with the latest data received
     */
    val pendingList: Deque<List<CurrencyRate>> = LinkedList()

    suspend fun updateList(newRatesList: List<CurrencyRate>)  {

        // If first list
        if (ratesList == null) {
            ratesList = newRatesList
            notifyItemRangeInserted(0, newRatesList.size)
            return
        }

        // Use DiffUtil to calculate the differences
        calculateDiff(newRatesList)
    }

    /** We call the DiffUtil callback using a coroutine to maximize
     *  UI performance with minimal lag
     */
    private suspend fun calculateDiff(latest: List<CurrencyRate>) {

        val ratesAdapter = this

        // Use Default for CPU intensive tasks
        val diffResult = withContext(Dispatchers.Default) { DiffUtil.calculateDiff(RatesDiff(ratesList!!, latest)) }

        val newRootRate: Boolean = (ratesList!![0].currency != latest[0].currency)
        ratesList = latest

        // Update UI on the Main Thread
        diffResult.dispatchUpdatesTo(ratesAdapter)
        if (newRootRate)
            callback.scrollToTop()

    }
}