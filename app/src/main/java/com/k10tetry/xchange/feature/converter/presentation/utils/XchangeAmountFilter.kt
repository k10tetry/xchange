package com.k10tetry.xchange.feature.converter.presentation.utils

import android.text.InputFilter
import android.text.Spanned
import com.k10tetry.xchange.feature.converter.common.validAmountFormat
import javax.inject.Inject

class XchangeAmountFilter @Inject constructor() : InputFilter {

    override fun filter(
        source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int
    ): CharSequence? {

        val stringBuilder = StringBuilder(dest.toString())
        stringBuilder.replace(dstart, dend, source?.subSequence(start, end).toString())
        try {
            if (stringBuilder.toString().validAmountFormat().not()) {
                return ""
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}