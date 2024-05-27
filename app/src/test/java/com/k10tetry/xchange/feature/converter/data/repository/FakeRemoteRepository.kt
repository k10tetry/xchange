package com.k10tetry.xchange.feature.converter.data.repository

import com.k10tetry.xchange.FileProvider
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository

class FakeRemoteRepository : XchangeRemoteRepository {
    override suspend fun getRates(): String? {
        return FileProvider.getJsonString("rates.json")
    }

    override suspend fun getCurrencies(): String? {
        return FileProvider.getJsonString("currency.json")
    }
}