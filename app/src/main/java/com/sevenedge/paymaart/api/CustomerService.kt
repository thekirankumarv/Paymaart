//CustomerService.kt
package com.sevenedge.paymaart.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerService {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int): Response<CustomerDetails>
}