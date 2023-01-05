package com.tinysoft.tmobiletest.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Description(
    val attributes: TextAttributes,
    val value: String
): Parcelable