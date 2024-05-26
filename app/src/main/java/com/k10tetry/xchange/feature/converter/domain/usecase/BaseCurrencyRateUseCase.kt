package com.k10tetry.xchange.feature.converter.domain.usecase

import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class BaseCurrencyRateUseCase @Inject constructor(
    private val localRepository: XchangeLocalRepository
) {

    /**
     * Return base currency and rate from local storage
     */
    suspend fun getBaseCurrencyRate(): Flow<Pair<String, String>> {
        return combine(
            localRepository.getBaseCurrency(),
            localRepository.getBaseRate()
        ) { currency, rate ->
            Pair(currency, rate)
        }
    }

    /**
     * Save base currency and rate to local storage
     */
    suspend fun saveBaseCurrencyRate(currency: Pair<String, String>) {
        localRepository.saveBaseCurrency(currency.first)
        localRepository.saveBaseRate(currency.second)
    }

}