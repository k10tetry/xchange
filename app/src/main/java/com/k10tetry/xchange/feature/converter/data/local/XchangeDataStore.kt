package com.k10tetry.xchange.feature.converter.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class XchangeDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val ratesPreferenceKey = stringPreferencesKey(RATES)
    private val currenciesPreferenceKey = stringPreferencesKey(CURRENCIES)
    private val baseCurrencyPreferenceKey = stringPreferencesKey(BASE_CURRENCY)
    private val baseRatePreferenceKey = stringPreferencesKey(BASE_RATE)

    companion object {
        const val RATES = "rates"
        const val CURRENCIES = "currencies"
        const val BASE_CURRENCY = "base_currency"
        const val BASE_RATE = "base_rate"
        const val DEFAULT_BASE_CURRENCY = "USD"
        const val DEFAULT_BASE_RATE = "1"
    }

    suspend fun saveRates(jsonString: String) {
        dataStore.edit { pref ->
            pref[ratesPreferenceKey] = jsonString
        }
    }

    suspend fun saveCurrencies(jsonString: String) {
        dataStore.edit { pref ->
            pref[currenciesPreferenceKey] = jsonString
        }
    }

    suspend fun saveBaseCurrency(currency: String) {
        dataStore.edit { pref ->
            pref[baseCurrencyPreferenceKey] = currency
        }
    }

    suspend fun saveBaseRate(rate: String) {
        dataStore.edit { pref ->
            pref[baseRatePreferenceKey] = rate
        }
    }

    suspend fun getRates(): String? {
        return dataStore.data.first()[ratesPreferenceKey]
    }

    suspend fun getCurrencies(): String? {
        return dataStore.data.first()[currenciesPreferenceKey]
    }

    fun getBaseCurrency(): Flow<String> {
        return dataStore.data.map {
            it[baseCurrencyPreferenceKey] ?: DEFAULT_BASE_CURRENCY
        }
    }

    fun getBaseRate(): Flow<String> {
        return dataStore.data.map {
            it[baseRatePreferenceKey] ?: DEFAULT_BASE_RATE
        }
    }

}