package com.hwx.productcare.api

import androidx.annotation.WorkerThread
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.api.requests.SignUpRequest
import retrofit2.Response
import retrofit2.http.*

interface FnsApi {

    @WorkerThread
    @GET
    suspend fun getCheck(
            @Url url: String,
            @HeaderMap headers: Map<String, String>,
            @Query("fiscalSign") fiscalSign: String,
            @Query("sendToEmail") sendToEmail: String
    ): Response<FnsCheck>

    @POST
    suspend fun signUp(
        @Url url: String,
        @Body requestData: SignUpRequest
    ): Response<String>
}