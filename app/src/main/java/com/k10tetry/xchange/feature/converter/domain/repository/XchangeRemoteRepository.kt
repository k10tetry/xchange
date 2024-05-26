package com.k10tetry.xchange.feature.converter.domain.repository

interface XchangeRemoteRepository {

    suspend fun getRates(): String?

    suspend fun getCurrencies(): String?

}