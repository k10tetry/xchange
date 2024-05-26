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
import com.k10tetry.xchange.databinding.ActivityXchangeBinding
import com.k10tetry.xchange.feature.converter.data.local.XchangeDataStore
import com.k10tetry.xchange.feature.converter.di.qualifier.GridLayout
import com.k10tetry.xchange.feature.converter.di.qualifier.LinearLayout
import com.k10tetry.xchange.feature.converter.presentation.ui.currency.CurrencyActivity
import com.k10tetry.xchange.feature.converter.presentation.utils.NetworkConnection
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeItemDecorator
import com.k10tetry.xchange.feature.converter.presentation.utils.hideKeyboard
import com.k10tetry.xchange.feature.converter.presentation.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class XchangeActivity : AppCompatActivity() {

    @Inject
    lateinit var xchangeAdapter: XchangeAdapter

    @Inject
    @GridLayout
    lateinit var gridLayoutManager: LayoutManager

    @Inject
    @LinearLayout
    lateinit var linearLayoutManager: LayoutManager

    @Inject
    lateinit var xchangeItemDecorator: XchangeItemDecorator

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var xchangeDataStore: XchangeDataStore

    private val xchangeViewModel by viewModels<XchangeViewModel>()

    private lateinit var binding: ActivityXchangeBinding

    private val countryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.extras?.let { bundle ->
                    xchangeViewModel.updateBaseCurrencyAndConvert(
                        bundle.getString("CURRENCY"),
                        binding.editTextAmount.text.toString()
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
                xchangeViewModel.currencyRateFlow.collect {
                    xchangeAdapter.countryRateList = it
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                xchangeViewModel.baseCurrencyFlow.collect {
                    binding.textViewCurrencyBase.text = it.first
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                xchangeViewModel.toastFlow.collect {
                    toast(it)
                }
            }
        }
    }

    private fun initViews() {

        binding.recycleViewXchange.adapter = xchangeAdapter
        binding.recycleViewXchange.layoutManager = gridLayoutManager
        binding.recycleViewXchange.addItemDecoration(xchangeItemDecorator)

        binding.textViewCurrencyBase.setOnClickListener {
            countryResult.launch(CurrencyActivity.getIntent(this))
        }

        binding.checkboxLayoutManager.setOnCheckedChangeListener { buttonView, isChecked ->
            xchangeAdapter.isGridLayout = isChecked
            binding.recycleViewXchange.layoutManager =
                if (isChecked) gridLayoutManager else linearLayoutManager
        }

        binding.editTextAmount.requestFocus()

        attachTextWatcher(binding.editTextAmount).debounce(DEBOUNCE_TIME).onEach {
            xchangeViewModel.convertCurrency(it.toString())
        }.launchIn(lifecycleScope)
    }

    private fun attachTextWatcher(editText: AppCompatEditText) = callbackFlow<CharSequence?> {

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                trySend(s.toString())
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
        const val DEBOUNCE_TIME = 600L
    }

}