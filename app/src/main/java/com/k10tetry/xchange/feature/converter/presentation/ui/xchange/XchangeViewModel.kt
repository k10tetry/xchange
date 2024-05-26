package com.k10tetry.xchange.feature.converter.presentation.ui.xchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k10tetry.xchange.feature.converter.domain.Resource
import com.k10tetry.xchange.feature.converter.domain.usecase.BaseCurrencyRateUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.GetRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class XchangeViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val baseCurrencyRateUseCase: BaseCurrencyRateUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _currencyRateFlow = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val currencyRateFlow = _currencyRateFlow.asStateFlow()

    private val _baseCurrencyFlow = MutableStateFlow(Pair("USD", "1"))
    val baseCurrencyFlow = _baseCurrencyFlow.asStateFlow()

    private var originalCurrencyRates = emptyList<Pair<String, String>>()

    private val _toastFlow = MutableSharedFlow<String>()
    val toastFlow = _toastFlow.asSharedFlow()

    fun getCurrencyRates() {
        viewModelScope.launch(dispatcher) {
            getRatesUseCase().collect {
                when (it) {
                    is Resource.Error -> {
                        _toastFlow.emit(it.message ?: "Something Wrong")
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        originalCurrencyRates = it.data ?: emptyList()
                    }
                }
            }
        }
    }

    fun getBaseCurrencyRates() {
        viewModelScope.launch(dispatcher) {
            baseCurrencyRateUseCase.getBaseCurrencyRate().collect {
                _baseCurrencyFlow.value = it
            }
        }
    }

    fun convertCurrency(amount: String?) {

        if (amount.isNullOrEmpty()) {
            _currencyRateFlow.value = emptyList()
            return
        }

        viewModelScope.launch(dispatcher) {
            convert(amount)
        }
    }

    private suspend fun convert(amount: String) {
        val fromRate = baseCurrencyRateUseCase.getBaseCurrencyRate().first().second.toDouble()
        _currencyRateFlow.value = originalCurrencyRates.map {
            val toRate = it.second.toDouble()
            val convertedAmount = amount.toDouble().times(toRate.div(fromRate))

            it.first to convertedAmount.toString()
        }
    }

    fun updateBaseCurrencyAndConvert(currency: String?, amount: String?) {

        if (currency.isNullOrEmpty() or amount.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch(dispatcher) {
            val updateCurrencyRate = originalCurrencyRates.first { rate -> rate.first == currency }
            baseCurrencyRateUseCase.saveBaseCurrencyRate(updateCurrencyRate)
            convert(amount!!)
        }
    }

}