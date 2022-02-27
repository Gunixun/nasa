package com.example.nasa.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
){
    Snackbar
        .make(this, text, length)
        .setAction(this.resources.getString(actionText), action)
        .show()
}
