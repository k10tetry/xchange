package com.k10tetry.xchange.feature.converter.presentation.ui.xchange

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.k10tetry.xchange.R
import com.k10tetry.xchange.databinding.ActivityXchangeBinding
import com.k10tetry.xchange.feature.converter.common.ExceptionType
import com.k10tetry.xchange.feature.converter.common.clear
import com.k10tetry.xchange.feature.converter.common.formatInputAmount
import com.k10tetry.xchange.feature.converter.di.qualifier.GridLayout
import com.k10tetry.xchange.feature.converter.di.qualifier.LinearLayout
import com.k10tetry.xchange.feature.converter.presentation.ui.currency.CurrencyActivity
import com.k10tetry.xchange.feature.converter.presentation.utils.NetworkConnection
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeAmountFilter
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeItemDecorator
import com.k10tetry.xchange.feature.converter.presentation.utils.hideKeyboard
import com.k10tetry.xchange.feature.converter.presentation.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class XchangeActivity : AppCompatActivity() {

    @Inject
    lateinit var xchangeAmountFilter: XchangeAmountFilter

    @Inject
    lateinit var xchangeAdapter: XchangeAdapter

    @Inject
    @GridLayout
    lateinit var gridLayoutManager: LayoutManager

    @Inject
    @LinearLayout
    lateinit var linearLayoutManager: LayoutManager

    @Inject
    lateinit var itemDecorator: XchangeItemDecorator

    @Inject
    lateinit var networkConnection: NetworkConnection

    private val xchangeViewModel by viewModels<XchangeViewModel>()

    private lateinit var binding: ActivityXchangeBinding

    private val currencyResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.extras?.getString(CURRENCY_CODE)?.let { currency ->
                    xchangeViewModel.updateAndConvert(
                        currency, binding.editTextAmount.text.toString()
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityXchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObservers()

        xchangeViewModel.getCurrencyRates()
        xchangeViewModel.getBaseCurrencyRates()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    xchangeViewModel.currencyRateFlow.collect {
                        xchangeAdapter.currencyRateList = it
                    }
                }

                launch {
                    xchangeViewModel.baseCurrencyFlow.collect {
                        binding.textViewCurrencyBase.text = it.first ?: "---"
                    }
                }

                launch {
                    xchangeViewModel.snackbarFlow.collect {
                        when (it) {
                            ExceptionType.NETWORK -> {
                                binding.root.snackbar(
                                    getString(R.string.network_connection_error),
                                    actionText = getString(R.string.retry)
                                ) {
                                    xchangeViewModel.getCurrencyRates()
                                }
                            }

                            ExceptionType.PARSING -> binding.root.snackbar(getString(R.string.parsing_error))
                            ExceptionType.COMMON -> binding.root.snackbar(getString(R.string.something_went_wrong))
                        }
                    }
                }

                // Convert currency after an interval/debounce
                launch {
                    attachTextWatcher(binding.editTextAmount).debounce(DEBOUNCE_TIME)
                        .collect { amount ->
                            if (amount.isNullOrEmpty().not()) {
                                xchangeViewModel.convertCurrency(amount.toString().clear())
                            } else {
                                xchangeViewModel.clearRates()
                            }
                        }
                }
            }
        }
    }

    private fun initViews() {

        binding.recycleViewXchange.adapter = xchangeAdapter
        binding.recycleViewXchange.layoutManager = gridLayoutManager
        binding.recycleViewXchange.addItemDecoration(itemDecorator)

        binding.textViewCurrencyBase.setOnClickListener {
            currencyResult.launch(CurrencyActivity.getIntent(this))
        }

        binding.checkboxLayoutManager.setOnCheckedChangeListener { _, isChecked ->
            xchangeAdapter.isGridLayout = isChecked
            binding.recycleViewXchange.layoutManager =
                if (isChecked) gridLayoutManager else linearLayoutManager
        }

        binding.editTextAmount.requestFocus()
        binding.editTextAmount.filters = arrayOf(xchangeAmountFilter)
    }

    private fun attachTextWatcher(editText: AppCompatEditText) = callbackFlow<CharSequence?> {

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isNotEmpty()) {
                    editText.removeTextChangedListener(this)
                    editText.setText(s.toString().clear().formatInputAmount())
                    editText.setSelection(editText.text?.toString()?.length ?: 0)
                    editText.addTextChangedListener(this)
                }

                trySend(s)
            }
        }

        editText.addTextChangedListener(watcher)

        awaitClose {
            editText.removeTextChangedListener(watcher)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (currentFocus is AppCompatEditText) {
                val rect = Rect().apply { currentFocus?.getGlobalVisibleRect(this) }
                if (!rect.contains(ev.x.toInt(), ev.y.toInt())) {
                    currentFocus?.clearFocus()
                    hideKeyboard(binding.root)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
        const val CURRENCY_CODE = "currency_code"
    }

}