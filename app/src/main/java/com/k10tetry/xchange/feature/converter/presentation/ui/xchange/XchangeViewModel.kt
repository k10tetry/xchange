package com.k10tetry.xchange.feature.converter.presentation.ui.xchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k10tetry.xchange.feature.converter.common.ExceptionType
import com.k10tetry.xchange.feature.converter.domain.usecase.BaseCurrencyRateUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.ConvertCurrency
import com.k10tetry.xchange.feature.converter.domain.usecase.GetRatesUseCase
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class XchangeViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val baseCurrencyRateUseCase: BaseCurrencyRateUseCase,
    private val convertCurrency: ConvertCurrency,
    private val dispatcher: XchangeDispatcher
) : ViewModel() {

    private val _currencyRateFlow = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val currencyRateFlow = _currencyRateFlow.asStateFlow()

    private val _baseCurrencyFlow = MutableStateFlow<Pair<String?, String?>>(Pair(null, null))
    val baseCurrencyFlow = _baseCurrencyFlow.asStateFlow()

    private var originalCurrencyRates = emptyList<Pair<String, String>>()

    private val _snackbarFlow = MutableSharedFlow<ExceptionType>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    fun getCurrencyRates() {
        viewModelScope.launch(dispatcher.io) {
            getRatesUseCase.fetchRates().catch { exception ->
                when (exception) {
                    is IOException -> _snackbarFlow.emit(ExceptionType.NETWORK)
                    is JSONException -> _snackbarFlow.emit(ExceptionType.PARSING)
                    else -> _snackbarFlow.emit(ExceptionType.COMMON)
                }
            }.collect {
                originalCurrencyRates = it
            }
        }
    }

    fun getBaseCurrencyRates() {
        viewModelScope.launch(dispatcher.io) {
            baseCurrencyRateUseCase.getBaseCurrencyRate().collect {
                _baseCurrencyFlow.value = it
            }
        }
    }

    fun convertCurrency(amount: String?) {
        viewModelScope.launch(dispatcher.io) {
            convert(amount)
        }
    }

    fun updateAndConvert(newCurrency: String, amount: String?) {
        viewModelScope.launch(dispatcher.io) {
            updateBaseCurrency(newCurrency)
            if (amount.isNullOrEmpty().not()) {
                convert(amount)
            }
        }
    }

    fun clearRates() {
        _currencyRateFlow.value = emptyList()
    }

    private suspend fun convert(amount: String?) = withContext(dispatcher.default) {
        val fromRate = baseCurrencyFlow.value.second
        _currencyRateFlow.value = originalCurrencyRates.map {
            it.first to convertCurrency(amount, it.second, fromRate)
        }
    }

    private suspend fun updateBaseCurrency(newCurrency: String) = withContext(dispatcher.io) {
        val rate = originalCurrencyRates.first { rate -> rate.first == newCurrency }.second
        baseCurrencyRateUseCase.saveBaseCurrencyRate(newCurrency to rate)
    }

}