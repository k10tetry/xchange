package com.k10tetry.xchange.feature.converter.presentation.utils

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.DecimalFormat

fun Activity.hideKeyboard(view: View?) {
    view?.let {
        val inputMethod =
            it.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Float.toPx(resources: Resources): Float = this * resources.displayMetrics.density

fun String.formatAmount(): String {
    val formatter = if (this.contains(".")) {
        DecimalFormat("###,###,##0.00")
    } else {
        DecimalFormat("###,###,###")
    }
    return formatter.format(this.toDoubleOrNull() ?: 0)
}