package com.example.testapp.services

import com.example.testapp.data.DataLogin
import com.example.testapp.respond.LoginRespond
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/local")
    fun getData(@Body body: DataLogin) : Call<LoginRespond>
}