package com.mobikasa.pagingsampleremote.api

import com.mobikasa.pagingsampleremote.models.ServiceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("popular")
    suspend fun getAllMovies(
        @Query("page") page: Long,
        @Query("api_key") api_key: String = "5370eed075b93dd79855f1c429b03ad8"
    ): ServiceResponse


}