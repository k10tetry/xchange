package com.k10tetry.xchange.feature.converter.presentation.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k10tetry.xchange.feature.converter.common.ExceptionType
import com.k10tetry.xchange.feature.converter.domain.usecase.BaseCurrencyRateUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.GetCurrenciesUseCase
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val baseCurrencyRateUseCase: BaseCurrencyRateUseCase,
    private val dispatcher: XchangeDispatcher
) : ViewModel() {

    private val _countryListFlow = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val countryListFlow = _countryListFlow.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<ExceptionType>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    var baseCurrencyRate: Pair<String, String>? = null

    fun getBaseAndCurrencyList() {
        viewModelScope.launch(dispatcher.io) {

            baseCurrencyRate = baseCurrencyRateUseCase.getBaseCurrencyRate().first()

            getCurrenciesUseCase.fetchCurrencies().catch { exception ->
                when (exception) {
                    is IOException -> _snackbarFlow.emit(ExceptionType.NETWORK)
                    is JSONException -> _snackbarFlow.emit(ExceptionType.PARSING)
                    else -> _snackbarFlow.emit(ExceptionType.COMMON)
                }
            }.collect {
                _countryListFlow.value = it
            }
        }
    }
}