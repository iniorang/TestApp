package com.example.testapp.services

import com.example.testapp.respond.UserRespond
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserServices {
    @GET("users")
    fun getData() : Call<List<UserRespond>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id : Int) : Call<UserRespond>
}