package com.k10tetry.xchange.feature.converter.domain.usecase

import com.k10tetry.xchange.feature.converter.domain.Resource
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class GetRatesUseCase @Inject constructor(
    private val remoteRepository: XchangeRemoteRepository,
    private val localRepository: XchangeLocalRepository
) {

    /**
     * This will return the list of currency rates from local storage
     * and in case local storage is empty fetch from remote and store
     * in local storage
     */
    operator fun invoke(): Flow<Resource<List<Pair<String, String>>>> = flow {
        try {
            localRepository.getRates()?.let {
                emit(Resource.Success(jsonToRatesList(it)))
            } ?: run {
                remoteRepository.getRates()?.let { rate ->
                    localRepository.saveRates(rate)
                    localRepository.getRates()?.let {
                        emit(Resource.Success(jsonToRatesList(it)))
                    }
                }
            }
        } catch (jsonEx: JSONException) {
            jsonEx.printStackTrace()
            emit(Resource.Error(message = "JSON Exception"))
        } catch (io: IOException) {
            io.printStackTrace()
            emit(Resource.Error(message = "IO Exception"))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(message = "Exception"))
        }
    }

    @Throws(JSONException::class)
    private fun jsonToRatesList(jsonString: String): List<Pair<String, String>> {
        val rates = mutableListOf<Pair<String, String>>()
        val jsonRates = JSONObject(jsonString).getJSONObject("rates")
        val keys = jsonRates.keys()
        while (keys.hasNext()) {
            val currency = keys.next()
            rates.add(currency to jsonRates.getString(currency))
        }
        return rates
    }

}