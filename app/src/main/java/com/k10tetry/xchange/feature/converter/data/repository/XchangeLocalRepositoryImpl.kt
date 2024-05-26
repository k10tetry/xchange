package com.k10tetry.xchange.feature.converter.data.repository

import com.k10tetry.xchange.feature.converter.data.local.XchangeDataStore
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XchangeLocalRepositoryImpl @Inject constructor(private val xchangeDataStore: XchangeDataStore) :
    XchangeLocalRepository {

    override suspend fun getRates(): String? {
        return xchangeDataStore.getRates()
    }

    override suspend fun getCurrencies(): String? {
        return xchangeDataStore.getCurrencies()
    }

    override suspend fun getBaseCurrency(): Flow<String> {
        return xchangeDataStore.getBaseCurrency()
    }

    override suspend fun getBaseRate(): Flow<String> {
        return xchangeDataStore.getBaseRate()
    }

    override suspend fun saveRates(jsonRates: String) {
        xchangeDataStore.saveRates(jsonRates)
    }

    override suspend fun saveCurrencies(jsonCurrencies: String) {
        xchangeDataStore.saveCurrencies(jsonCurrencies)
    }

    override suspend fun saveBaseCurrency(currency: String) {
        xchangeDataStore.saveBaseCurrency(currency)
    }

    override suspend fun saveBaseRate(rate: String) {
        xchangeDataStore.saveBaseRate(rate)
    }

}