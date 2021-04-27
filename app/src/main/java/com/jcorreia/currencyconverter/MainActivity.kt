package com.jcorreia.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jcorreia.currencyconverter.ui.RatesFragment
import com.jcorreia.currencyconverter.viewmodel.RatesViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init our ViewModel and fetch latest data
        val ratesViewModel: RatesViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application)).get(RatesViewModel::class.java)
        ratesViewModel.refreshRates()

        displayFragmentRates()
    }

    private fun displayFragmentRates() {

        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        fragmentTransaction.replace(R.id.mainFrameLayout, RatesFragment.newInstance())
        fragmentTransaction.commit()
    }

}

