package com.k10tetry.xchange.feature.converter.domain.repository

import kotlinx.coroutines.flow.Flow

interface XchangeLocalRepository {

    suspend fun getRates(): String?

    suspend fun getCurrencies(): String?

    suspend fun getBaseCurrency(): Flow<String>

    suspend fun getBaseRate(): Flow<String>

    suspend fun saveRates(jsonRates: String)

    suspend fun saveCurrencies(jsonCurrencies: String)

    suspend fun saveBaseCurrency(currency: String)

    suspend fun saveBaseRate(rate: String)
}