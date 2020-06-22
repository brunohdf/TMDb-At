package com.arctouch.codechallenge.extension

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun AppCompatImageView.loadRemoteImage(context: Context?, url: String) {
    context?.let {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(this)
    }
}