package com.arctouch.codechallenge.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object MessageUtil {
    fun displayError(context: Context?, @StringRes messageId: Int) {
        context?.let {
            Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show()
        }
    }
}