package com.tinysoft.tmobiletest.network.model

import com.google.gson.annotations.SerializedName

data class CardJson(
    @SerializedName("card")
    val jsonCard: Map<String, Any>,
    @SerializedName("card_type")
    val cardType: String
)