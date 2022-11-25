package com.femi.e_class

import android.view.View
import androidx.appcompat.app.AlertDialog


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.disable(disable: Boolean) {
    isEnabled = !disable
}

fun AlertDialog.showLoadingDialog(enable: Boolean) = when (enable) {
    true -> show()
    false -> dismiss()
}