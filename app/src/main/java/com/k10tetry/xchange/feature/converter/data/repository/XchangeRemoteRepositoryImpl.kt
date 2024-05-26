package com.k10tetry.xchange.feature.converter.data.repository

import com.k10tetry.xchange.feature.converter.data.remote.XchangeCurrencyApi
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class XchangeRemoteRepositoryImpl @Inject constructor(
    private val xchangeCurrencyApi: XchangeCurrencyApi
) : XchangeRemoteRepository {

    @Throws(IOException::class)
    override suspend fun getRates(): String? {
        return xchangeCurrencyApi.getRates().string()
    }

    @Throws(IOException::class)
    override suspend fun getCurrencies(): String? {
        return xchangeCurrencyApi.getCurrencies().string()
    }
}