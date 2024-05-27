package com.k10tetry.xchange.feature.converter.presentation.ui.currency

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.snackbar.Snackbar
import com.k10tetry.xchange.R
import com.k10tetry.xchange.databinding.ActivityCurrencyBinding
import com.k10tetry.xchange.feature.converter.common.ExceptionType
import com.k10tetry.xchange.feature.converter.di.qualifier.LinearLayout
import com.k10tetry.xchange.feature.converter.presentation.ui.xchange.XchangeActivity
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeItemDecorator
import com.k10tetry.xchange.feature.converter.presentation.utils.snackbar
import com.k10tetry.xchange.feature.converter.presentation.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyActivity : AppCompatActivity() {

    @Inject
    lateinit var currencyAdapter: CurrencyAdapter

    @Inject
    @LinearLayout
    lateinit var layoutManager: LayoutManager

    @Inject
    lateinit var xchangeItemDecorator: XchangeItemDecorator

    private lateinit var binding: ActivityCurrencyBinding

    private val currencyViewModel: CurrencyViewModel by viewModels<CurrencyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObserver()

        currencyViewModel.getBaseAndCurrencyList()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    currencyViewModel.countryListFlow.collect {
                        currencyAdapter.selectedCurrency = currencyViewModel.baseCurrencyRate?.first
                        currencyAdapter.countryList = it
                    }
                }

                launch {
                    currencyViewModel.snackbarFlow.collect {
                        when (it) {
                            ExceptionType.NETWORK -> {
                                binding.root.snackbar(
                                    getString(R.string.network_connection_error),
                                    actionText = getString(R.string.retry),
                                    duration = Snackbar.LENGTH_LONG
                                ) {
                                    currencyViewModel.getBaseAndCurrencyList()
                                }
                            }

                            ExceptionType.PARSING -> binding.root.snackbar(getString(R.string.parsing_error))
                            ExceptionType.COMMON -> binding.root.snackbar(getString(R.string.something_went_wrong))
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.recycleViewCurrency.adapter = currencyAdapter
        binding.recycleViewCurrency.layoutManager = layoutManager
        binding.recycleViewCurrency.addItemDecoration(xchangeItemDecorator)

        currencyAdapter.currencyListener = currencyListener
    }

    private val currencyListener = object : CurrencyAdapter.CurrencyListener {
        override fun onClick() {
            lifecycleScope.launch {
                delay(500)
                Intent().apply {
                    putExtra(XchangeActivity.CURRENCY_CODE, currencyAdapter.selectedCurrency)
                }.run {
                    setResult(RESULT_OK, this)
                    finish()
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CurrencyActivity::class.java)
        }
    }

}