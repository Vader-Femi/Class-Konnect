package com.femi.e_class

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.femi.e_class.presentation.LogInFormEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

suspend inline fun SnackbarHostState.handleNetworkExceptions(
    exception: java.lang.Exception? = null,
    message: String? = null,
    retry: (() -> Unit),
) {

    val snackbarResult = showSnackbar(
        message = exception?.message ?: message.toString(),
        actionLabel = "Retry"
    )

    when (snackbarResult) {
        SnackbarResult.Dismissed -> {}
        SnackbarResult.ActionPerformed -> {retry()}
    }

}

fun View.hideKeyboard() {
    this.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}