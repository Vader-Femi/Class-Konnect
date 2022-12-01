package com.femi.e_class

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.disable(disable: Boolean) {
    isEnabled = !disable
}

fun Context.loadingDialog(view: View, title: String, message: String): AlertDialog {
    return MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setView(view)
        .setMessage(message)
        .setCancelable(false)
        .create()
}

fun AlertDialog.showLoadingDialog(enable: Boolean) = when (enable) {
    true -> show()
    false -> dismiss()
}

fun Fragment.handleNetworkExceptions(exception: java.lang.Exception?, retry: (() -> Unit)? = null){
//    Toast.makeText(this.context,
//        exception?.message.toString(),
//        Toast.LENGTH_SHORT)
//        .show()

    view?.snackbar(
        exception?.message.toString(),
        retry
    )

}

fun View.handleNetworkExceptions(exception: java.lang.Exception? = null, retry: (() -> Unit)? = null, message: String? = null){

    hideKeyboard()
//    Toast.makeText(this.context,
//        exception?.message.toString(),
//        Toast.LENGTH_SHORT)
//        .show()

    when {
        !message.isNullOrEmpty() -> {
            snackbar(
                message.toString(),
                retry
            )
        }
        exception != null -> {
            snackbar(
                exception.message.toString(),
                retry
            )
        }
        else -> {
            snackbar(
               "We experienced an error, Please try again",
                retry
            )
        }
    }

}

fun View.hideKeyboard() {
    this.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    if (!snackbar.isShownOrQueued)
        snackbar.show()
}