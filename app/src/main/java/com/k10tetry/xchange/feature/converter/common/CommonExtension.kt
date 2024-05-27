package com.k10tetry.xchange.feature.converter.common

import java.text.DecimalFormat
import java.util.regex.Pattern

fun String.formatAmount(): String {
    val formatter = if (this.contains(".")) {
        DecimalFormat("##,##,##,###.${this.decimalPattern()}")
    } else {
        DecimalFormat("##,##,##,###")
    }
    return formatter.format(this.toDoubleOrNull() ?: 0)
}

fun String.decimalPattern(): String {
    val decimalCount = this.length - 1 - this.indexOf(".")
    val decimalPattern = StringBuilder()
    var i = 0
    while (i < decimalCount && i < 2) {
        decimalPattern.append("0")
        i++
    }
    return decimalPattern.toString()
}

fun String.clear(): String {
    return this.replace(",", "")
}

fun String.validAmountFormat(): Boolean {
    val pattern = Pattern.compile("(([1-9]{1})([0-9,]{0,${8}})?(?:\\.[0-9]{0,2})?)")
    return pattern.matcher(this).matches()
}