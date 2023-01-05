package com.tinysoft.tmobiletest.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val size: Size,
    val url: String
): Parcelable