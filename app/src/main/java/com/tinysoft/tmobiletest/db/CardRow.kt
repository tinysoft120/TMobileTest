package com.tinysoft.tmobiletest.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.tinysoft.tmobiletest.network.model.Card
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "card_table")
@Parcelize
data class CardRow(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val index: Int,

    @ColumnInfo(name = "json_text")
    var cardDetails: String,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date,
) : Parcelable {
    companion object {
        fun fromCard(index: Int, card: Card, updatedAt: Date = Date()): CardRow {
            val gson = Gson()
            val json = gson.toJson(card)
            return CardRow(index, json, updatedAt)
        }
    }

    fun toCard(): Card {
        val gson = Gson()
        return gson.fromJson(this.cardDetails, Card::class.java)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is CardRow) {
            if (!Objects.equals(this.cardDetails, other.cardDetails)) return false
            // ignore updatedAt when check equal objects
            //if (!Objects.equals(this.updatedAt, other.updatedAt)) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + cardDetails.hashCode()
        //result = 31 * result + updatedAt.hashCode()
        return result
    }
}
