

package com.example.mygragment

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DashboardApi {
    @GET("dashboard/animals")
    suspend fun getEntities(@Query("keypass") keypass: String): Response<DashboardResponse>
}