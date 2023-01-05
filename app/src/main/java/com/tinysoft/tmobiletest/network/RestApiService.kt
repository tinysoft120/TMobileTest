package com.tinysoft.tmobiletest.network

import com.tinysoft.tmobiletest.network.model.HomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface RestApiService {
    @GET("test/home")
    suspend fun homeList(): Response<HomeResponse>
}