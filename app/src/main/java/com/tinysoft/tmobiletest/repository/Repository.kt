package com.tinysoft.tmobiletest.repository

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.tinysoft.tmobiletest.db.CardDao
import com.tinysoft.tmobiletest.db.CardRow
import com.tinysoft.tmobiletest.network.RestApiService
import com.tinysoft.tmobiletest.network.ResultWrapper
import com.tinysoft.tmobiletest.network.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * A Repository is an interface that abstracts access to network data sources.
 */
interface Repository {
    /**
     * Get T-Mobile Home list via rest api.
     * Return result wrapper for list of Card.
     * This method is suspendable synchronous.
     * @return Response wrapper
     */
    suspend fun getHomeList() : ResultWrapper<List<Card>>

    /**
     * Store Home card list data from api [Card]s into Database.
     * @param cards Card details from api
     */
    suspend fun storeCardList(cards: List<Card>)

    /**
     * Get all Card data from local databases.
     * Return list of [Card].
     * This method is synchronous.
     */
    suspend fun getCacheCardAll(): List<Card>
}

/**
 * Implementation class of Repository
 */
class RepositoryImpl(
    private val apiService: RestApiService,
    private val cardDao: CardDao
) : Repository {

    @WorkerThread
    override suspend fun getHomeList(): ResultWrapper<List<Card>> {
        val w = safeApiCall { apiService.homeList() }
        return when (w) {
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            is ResultWrapper.GenericError -> ResultWrapper.GenericError(w.code, w.message)
            is ResultWrapper.Success -> {
                val result: MutableList<Card> = mutableListOf()
                val gson = Gson()
                for (itemCard in w.value.page.cards) {
                    val jsonCard = gson.toJson(itemCard.jsonCard)
                    when (itemCard.cardType) {
                        "text", "title" -> {
                            val title = gson.fromJson(jsonCard, Title::class.java)
                            result.add(Card(title))
                        }
                        "description" -> {
                            val description = gson.fromJson(jsonCard, Description::class.java)
                            result.add(Card(null, description, null))
                        }
                        "image" -> {
                            val image = gson.fromJson(jsonCard, Image::class.java)
                            result.add(Card(null, null, image))
                        }
                        else -> {
                            val card = gson.fromJson(jsonCard, Card::class.java)
                            result.add(card)
                        }
                    }
                }
                ResultWrapper.Success(result)
            }
        }
    }

    private suspend fun <T : Any> safeApiCall(apiToBeCalled: suspend () -> Response<T>): ResultWrapper<T> {
        val response: Response<T>
        try {
            response = apiToBeCalled.invoke()
        } catch (t: Throwable) {
            // TODO: should handle error depend on Exception type. Ex: IOException, HttpException, SocketTimeoutException, ...
            return when(t) {
                is CancellationException -> throw t // if coroutine is cancelled, ignore it
                is IOException -> {
                    println("No internet connection")
                    ResultWrapper.NetworkError
                }
                is HttpException -> {
                    println("Something went wrong from server")
                    ResultWrapper.GenericError(1002, "Something went wrong from server")
                }
                is SocketTimeoutException -> {
                    println("Server connection error")
                    ResultWrapper.NetworkError
                }
                else -> {
                    println("Ignore unknown error")
                    ResultWrapper.NetworkError
                }
            }
        }

        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ResultWrapper.Success(body)
            } else {
                // Empty response error
                ResultWrapper.GenericError(1001, "response.body() can't be null")
            }
        } else {
            val errorBody = response.errorBody()?.charStream()?.readLines()
            print("Api Error response: $errorBody")
            // TODO: we can parse error formatted response
            ResultWrapper.GenericError(response.code(), response.message())
        }
    }

    @WorkerThread
    override suspend fun storeCardList(cards: List<Card>) {
        // remove all previous data
        cardDao.deleteAll()

        // convert to DB rows
        val rows = cards.mapIndexed { i, card -> CardRow.fromCard(i, card) }
        cardDao.storeAllCards(rows)
    }

    override suspend fun getCacheCardAll(): List<Card> = cardDao.getAllCards().map { it.toCard() }
}