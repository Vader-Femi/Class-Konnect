package com.femi.e_class

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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