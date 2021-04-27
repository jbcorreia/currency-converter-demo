package com.jcorreia.currencyconverter.ui

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jcorreia.currencyconverter.databinding.FragmentRatesBinding
import com.jcorreia.currencyconverter.viewmodel.RatesViewModel
import kotlinx.coroutines.*

class RatesFragment : Fragment(), RatesAdapter.OnRateInteraction {

    private var _binding : FragmentRatesBinding? = null
    private val binding get() = _binding!!

    private var ratesAdapter: RatesAdapter? = null
    private var ratesViewModel: RatesViewModel? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ratesViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity!!.application)).get(RatesViewModel::class.java)

        observeModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentRatesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesAdapter = RatesAdapter(this)
        binding.ratesRecyclerView.adapter = ratesAdapter

        val mLayoutManager: RecyclerView.LayoutManager
        mLayoutManager = LinearLayoutManager(context)
        binding.ratesRecyclerView.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(
                binding.ratesRecyclerView.getContext(),
                mLayoutManager.orientation
        )
        binding.ratesRecyclerView.addItemDecoration(mDividerItemDecoration)

    }

    private fun observeModel() {

        // Get latest rates and pass it over to the adapter
        ratesViewModel?.getCurrencyRates()?.observe(viewLifecycleOwner,
                Observer {currencyRates ->
                    if (currencyRates != null) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            ratesAdapter?.updateList(currencyRates)
                        }
                    }
                })

        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(1000)
                ratesViewModel?.refreshRates()
            }
        }
    }

    override fun scrollToTop() {
        binding.ratesRecyclerView.scrollToPosition(0)
    }

    override fun onRateChanged(currencyName: String, value: Float) {
        ratesViewModel?.setNewBase(currencyName,value)
    }

    override fun onValueChanged(value: Float) {
        ratesViewModel?.setNewBaseValue(value)
    }

    companion object {
        fun newInstance(): RatesFragment {
            return RatesFragment()
        }
    }
}