@file:Suppress("unused")

package com.petrunnel.rickandmorty.extentions

import android.content.Context
import android.widget.Toast

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}