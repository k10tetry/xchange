package com.k10tetry.xchange.feature.converter.domain.usecase

import javax.inject.Inject

class ConvertCurrency @Inject constructor() {

    operator fun invoke(amount: String?,toRate: String?,fromRate: String?): String {
        return if (validate(amount) || validate(fromRate) || validate(toRate)) {
            "NaN"
        } else {
            amount!!.toDouble().times(toRate!!.toDouble().div(fromRate!!.toDouble())).toString()
        }
    }

    private fun validate(value: String?) =
        value.isNullOrEmpty() || value.toDoubleOrNull() == null

}