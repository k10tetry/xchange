package com.k10tetry.xchange.feature.converter.domain.usecase

import com.google.common.truth.Truth
import org.junit.Test

class ConvertCurrencyUseCaseTest {

    private val currencyUseCase: ConvertCurrencyUseCase = ConvertCurrencyUseCase()

    @Test
    fun `Invalid amount, toRate, fromRate value, return NaN`() {
        Truth.assertThat(currencyUseCase("", "", "")).isEqualTo("NaN")
        Truth.assertThat(currencyUseCase(null, null, null)).isEqualTo("NaN")
        Truth.assertThat(currencyUseCase("asdf", "asdf", "asdf")).isEqualTo("NaN")
    }

    @Test
    fun `Valid amount, toRate, fromRate value, return result`() {
        Truth.assertThat(currencyUseCase("1", "1", "1")).isEqualTo("1.0")
        Truth.assertThat(currencyUseCase("0", "83.09", "1")).isEqualTo("0.0")
        Truth.assertThat(currencyUseCase("1", "83.09", "1")).isEqualTo("83.09")
    }

}