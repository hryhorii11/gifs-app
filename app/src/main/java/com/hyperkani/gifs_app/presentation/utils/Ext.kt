package com.hyperkani.gifs_app.presentation.utils

import android.content.Context
import android.widget.Toast

fun Context.showMessage(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}