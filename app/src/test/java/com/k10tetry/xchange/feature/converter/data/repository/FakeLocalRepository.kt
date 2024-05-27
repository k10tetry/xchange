package com.k10tetry.xchange.feature.converter.data.repository

import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalRepository : XchangeLocalRepository {

    var baseCurrency = "USD"
    var baseRate = "1"
    var rates: String? = null
    var currency: String? = null

    override suspend fun getRates(): String? {
        return rates
    }

    override suspend fun getCurrencies(): String? {
        return currency
    }

    override suspend fun getBaseCurrency(): Flow<String> = flow {
        emit(baseCurrency)
    }

    override suspend fun getBaseRate(): Flow<String> = flow {
        emit(baseRate)
    }

    override suspend fun saveRates(jsonRates: String) {
        rates = jsonRates
    }

    override suspend fun saveCurrencies(jsonCurrencies: String) {
        currency = jsonCurrencies
    }

    override suspend fun saveBaseCurrency(currency: String) {
        baseCurrency = currency
    }

    override suspend fun saveBaseRate(rate: String) {
        baseRate = rate
    }
}