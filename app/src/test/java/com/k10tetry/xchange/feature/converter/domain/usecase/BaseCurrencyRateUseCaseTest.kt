package com.k10tetry.xchange.feature.converter.domain.usecase

import com.google.common.truth.Truth
import com.k10tetry.xchange.feature.converter.data.repository.FakeLocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BaseCurrencyRateUseCaseTest {

    private val localRepository = FakeLocalRepository()
    private val baseCurrencyRateUseCase = BaseCurrencyRateUseCase(localRepository)

    @Test
    fun saveBaseCurrencyRate_returnResult() = runTest {
        val input = "INR" to "1"
        baseCurrencyRateUseCase.saveBaseCurrencyRate(input)
        Truth.assertThat(baseCurrencyRateUseCase.getBaseCurrencyRate().first()).isEqualTo(input)
    }


}