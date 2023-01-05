package com.tinysoft.tmobiletest.ui.homelist

import android.os.Parcelable
import com.tinysoft.tmobiletest.network.model.Card
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeListViewState(
    val totalCount: Int,
    val items: List<Card>
): Parcelable {
    companion object {
        val EMPTY = HomeListViewState(0, listOf())
    }
}