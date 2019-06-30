package com.jcorreia.currencyconverter.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcorreia.currencyconverter.R
import com.jcorreia.currencyconverter.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.coroutines.*

class RatesFragment : Fragment(), RatesAdapter.OnRateInteraction {

    private var ratesAdapter: RatesAdapter? = null
    private var ratesViewModel: RatesViewModel? = null

    private var fragmentActive = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ratesViewModel = ViewModelProviders.of(activity!!).get(RatesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate (R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesAdapter = RatesAdapter(this)
        ratesRecyclerView.adapter = ratesAdapter

        val mLayoutManager: RecyclerView.LayoutManager
        mLayoutManager = LinearLayoutManager(context)
        ratesRecyclerView.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(
                ratesRecyclerView.getContext(),
                mLayoutManager.orientation
        )
        ratesRecyclerView.addItemDecoration(mDividerItemDecoration)

        observeModel()
    }

    private fun observeModel() {

        // Get latest rates and pass it over to the adapter
        ratesViewModel?.getCurrencyRates()?.observe(this,
                Observer {currencyRates ->
                    if (currencyRates != null) {
                        GlobalScope.launch(Dispatchers.Main) {
                            ratesAdapter?.updateList(currencyRates)
                        }
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        fragmentActive=true
        GlobalScope.launch(Dispatchers.Main) {
            refreshThread()
        }
    }

    suspend fun refreshThread() = withContext(Dispatchers.Main) {
        while (fragmentActive) {
            delay(1000)
            ratesViewModel?.refreshRates()
        }
    }

    override fun onStop() {
        super.onStop()
        fragmentActive=false
    }

    override fun scrollToTop() {
        ratesRecyclerView.scrollToPosition(0)
    }

    override fun onRateChanged(currencyName: String, value: Float) {
        ratesViewModel?.setNewBase(currencyName,value)
    }

    override fun onValueChanged(value: Float) {
        ratesViewModel?.setNewBaseValue(value)
    }

    companion object {
        fun newInstance(): RatesFragment {
            val fragment = RatesFragment()
            return fragment
        }
    }
}