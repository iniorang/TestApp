package com.example.testapp.services

import com.example.testapp.data.DataRegister
import com.example.testapp.respond.LoginRespond
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("auth/local/register")
    fun saveData(@Body body: DataRegister) : Call<LoginRespond>
}