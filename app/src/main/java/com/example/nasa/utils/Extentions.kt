package com.example.nasa.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.hideSnackBar(snackbar: Snackbar?){
    if (snackbar != null && snackbar.isShown){
        snackbar.dismiss()
    }
}


fun View.createErrSnackBar(
    text: String,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    return Snackbar
        .make(this, text, length)
        .setAction(this.resources.getString(actionText), action)
}


fun View.createMsgSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    return Snackbar.make(this, text, length)
}