package com.tinysoft.tmobiletest.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    val height: Int,
    val width: Int
): Parcelable