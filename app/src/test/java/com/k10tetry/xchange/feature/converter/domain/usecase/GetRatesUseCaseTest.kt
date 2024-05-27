package com.k10tetry.xchange.feature.converter.domain.usecase

import com.google.common.truth.Truth
import com.k10tetry.xchange.feature.converter.data.repository.FakeLocalRepository
import com.k10tetry.xchange.feature.converter.data.repository.FakeRemoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetRatesUseCaseTest {

    private val localRepository = FakeLocalRepository()
    private val remoteRepository = FakeRemoteRepository()
    private val getRatesUseCase = GetRatesUseCase(remoteRepository, localRepository)

    @Test
    fun `Fetch currencies, return result`() = runTest(testBody = suspendFunction1())

    private fun suspendFunction1(): suspend TestScope.() -> Unit = {
        Truth.assertThat(getRatesUseCase.fetchRates().first()).isNotEmpty()
    }
}