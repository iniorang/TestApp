package com.example.testapp.services

import com.example.testapp.respond.UserGet
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserServices {
    @GET("users")
    fun getData() : Call<List<UserGet>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id : Int) : Call<UserGet>
}