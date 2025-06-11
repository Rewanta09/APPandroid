package com.example.mygragment

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("sydney/auth")
    suspend fun authenticate(@Body credentials: Credentials): Response<AuthResponse>
}