package com.femi.e_class.data

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.lang.Exception

fun Context.handleNetworkExceptions(
    exception: Exception? = null,
    message: String? = null
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