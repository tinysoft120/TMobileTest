package com.tinysoft.tmobiletest.network.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val title: Title? = null,
    val description: Description? = null,
    val image: Image? = null
): Parcelable {
    @IgnoredOnParcel
    val hasImage: Boolean get() = image != null
}