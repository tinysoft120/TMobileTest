package com.tinysoft.tmobiletest.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextAttributes(
    val font: Font,
    @SerializedName("text_color")
    val textColor: String
): Parcelable