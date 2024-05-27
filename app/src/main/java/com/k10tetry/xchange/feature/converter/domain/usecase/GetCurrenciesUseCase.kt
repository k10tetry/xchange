package com.k10tetry.xchange.feature.converter.domain.usecase

import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val remoteRepository: XchangeRemoteRepository,
    private val localRepository: XchangeLocalRepository
) {

    /**
     * This will return the list of currencies from local storage
     * and in case local storage is empty fetch from remote and store
     * in local storage
     */
    fun fetchCurrencies(): Flow<List<Pair<String, String>>> = flow {
        localRepository.getCurrencies()?.let {
            emit(jsonToCurrencyList(it))
        } ?: remoteRepository.getCurrencies()?.let {
            localRepository.saveCurrencies(it)
            localRepository.getCurrencies()?.let { curr ->
                emit(jsonToCurrencyList(curr))
            }
        } ?: run {
            emit(emptyList())
        }
    }

    suspend fun fetchAndSaveCurrencies() {
        remoteRepository.getCurrencies()?.let {
            localRepository.saveCurrencies(it)
        }
    }

    @Throws(JSONException::class)
    private fun jsonToCurrencyList(jsonString: String): List<Pair<String, String>> {
        val currencies = mutableListOf<Pair<String, String>>()
        val jsonRates = JSONObject(jsonString)
        val keys = jsonRates.keys()
        while (keys.hasNext()) {
            val currency = keys.next()
            currencies.add(currency to jsonRates.optDouble(currency).toString())
        }
        return currencies
    }

}