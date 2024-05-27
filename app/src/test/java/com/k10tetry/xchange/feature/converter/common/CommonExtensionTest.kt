package com.k10tetry.xchange.feature.converter.common

import com.google.common.truth.Truth
import org.junit.Test

class CommonExtensionTest {

    @Test
    fun `Format input amount for valid input values`() {
        Truth.assertThat("1".formatInputAmount()).isEqualTo("1")
        Truth.assertThat("10000.0".formatInputAmount()).isEqualTo("10,000.0")
        Truth.assertThat("10000.".formatInputAmount()).isEqualTo("10,000.")
        Truth.assertThat("10000.435".formatInputAmount()).isEqualTo("10,000.43")
    }

    @Test
    fun `Format input amount for invalid input values`() {
        Truth.assertThat("".formatInputAmount()).isEqualTo("0")
        Truth.assertThat(" ".formatInputAmount()).isEqualTo("0")
        Truth.assertThat("asdf".formatInputAmount()).isEqualTo("0")
    }

    @Test
    fun `Format rates for valid input values`() {
        Truth.assertThat("1".formatRates()).isEqualTo("1")
        Truth.assertThat("0.0".formatRates()).isEqualTo("0")
        Truth.assertThat("10000.0".formatRates()).isEqualTo("10,000")
        Truth.assertThat("10000.".formatRates()).isEqualTo("10,000")
        Truth.assertThat("10000.435".formatRates()).isEqualTo("10,000.43")
    }

    @Test
    fun `Format rates for invalid input values`() {
        Truth.assertThat("".formatRates()).isEqualTo("0")
        Truth.assertThat(" ".formatRates()).isEqualTo("0")
        Truth.assertThat("asdf".formatRates()).isEqualTo("0")
    }

    @Test
    fun `Decimal pattern for input values`() {
        Truth.assertThat("10.00".decimalPattern()).isEqualTo("00")
        Truth.assertThat("10.000".decimalPattern()).isEqualTo("00")
        Truth.assertThat("10.0".decimalPattern()).isEqualTo("0")
        Truth.assertThat(" ".formatRates()).isEqualTo("0")
        Truth.assertThat("asdf".formatRates()).isEqualTo("0")
    }

    @Test
    fun `Clear comma from string for input values`() {
        Truth.assertThat("10,000.0".clear()).isEqualTo("10000.0")
        Truth.assertThat("10,000".clear()).isEqualTo("10000")
        Truth.assertThat("10.0".clear()).isEqualTo("10.0")
        Truth.assertThat(" ".clear()).isEqualTo(" ")
        Truth.assertThat("asdf".clear()).isEqualTo("asdf")
    }

    @Test
    fun `Valid amount format for input values`() {
        Truth.assertThat("0".validAmountFormat()).isFalse()
        Truth.assertThat(".".validAmountFormat()).isFalse()
        Truth.assertThat(",".validAmountFormat()).isFalse()
        Truth.assertThat("0123".validAmountFormat()).isFalse()
        Truth.assertThat("10.0".validAmountFormat()).isTrue()
        Truth.assertThat(" ".validAmountFormat()).isFalse()
        Truth.assertThat("".validAmountFormat()).isFalse()
        Truth.assertThat("asdf".validAmountFormat()).isFalse()
        Truth.assertThat("10,000.00".validAmountFormat()).isTrue()
        Truth.assertThat("10,000.123".validAmountFormat()).isFalse()
        Truth.assertThat("1234567890.00".validAmountFormat()).isFalse()
    }


}