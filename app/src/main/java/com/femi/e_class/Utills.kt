package com.femi.e_class

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.handleNetworkExceptions(
    exception: java.lang.Exception? = null,
    message: String? = null,
    retry: (() -> Unit),
) {

    Toast.makeText(
        this,
        exception?.message ?: message,
        Toast.LENGTH_SHORT).show()

}

fun View.hideKeyboard() {
    this.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}