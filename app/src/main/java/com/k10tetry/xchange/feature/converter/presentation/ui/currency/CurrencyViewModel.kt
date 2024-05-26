package com.k10tetry.xchange.feature.converter.presentation.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k10tetry.xchange.feature.converter.domain.usecase.BaseCurrencyRateUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val baseCurrencyRateUseCase: BaseCurrencyRateUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _countryListFlow = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val countryListFlow = _countryListFlow.asStateFlow()

    var baseCurrencyRate: Pair<String, String>? = null

    fun getBaseAndCurrencyList() {
        viewModelScope.launch(dispatcher) {
            baseCurrencyRate = baseCurrencyRateUseCase.getBaseCurrencyRate().first()
            _countryListFlow.value = getCurrenciesUseCase().first()
        }
    }
}