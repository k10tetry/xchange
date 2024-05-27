package com.k10tetry.xchange.feature.converter.domain.usecase

import com.google.common.truth.Truth
import com.k10tetry.xchange.feature.converter.data.repository.FakeLocalRepository
import com.k10tetry.xchange.feature.converter.data.repository.FakeRemoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCurrenciesUseCaseTest {

    private val localRepository = FakeLocalRepository()
    private val remoteRepository = FakeRemoteRepository()
    private val getCurrenciesUseCase = GetCurrenciesUseCase(remoteRepository, localRepository)

    @Test
    fun `Fetch rate, return result`() = runTest {
        Truth.assertThat(getCurrenciesUseCase.fetchCurrencies().first()).isNotEmpty()
    }


}