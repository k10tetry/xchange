package com.k10tetry.xchange.feature.converter.domain.usecase

import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
    private val remoteRepository: XchangeRemoteRepository,
    private val localRepository: XchangeLocalRepository
) {

    /**
     * This will return the list of currency rates from local storage
     * and in case local storage is empty fetch from remote and store
     * in local storage
     */
    fun fetchRates(): Flow<List<Pair<String, String>>> = flow {
        localRepository.getRates()?.let {
            emit(jsonToRatesList(it))
        } ?: remoteRepository.getRates()?.let {

            localRepository.saveRates(it)
            localRepository.getRates()?.let { rates ->
                emit(jsonToRatesList(rates))
            }
        } ?: run {
            emit(emptyList())
        }
    }

    suspend fun fetchAndSaveRates() {
        remoteRepository.getRates()?.let {
            localRepository.saveRates(it)
        }
    }

    @Throws(JSONException::class)
    private fun jsonToRatesList(jsonString: String): List<Pair<String, String>> {
        val rates = mutableListOf<Pair<String, String>>()
        val jsonRates = JSONObject(jsonString).getJSONObject("rates")
        val keys = jsonRates.keys()
        while (keys.hasNext()) {
            val currency = keys.next()
            rates.add(currency to jsonRates.optDouble(currency).toString())
        }
        return rates
    }

}