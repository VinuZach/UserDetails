package com.example.myapplication.apiManager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager
{
    fun createManagerInstance(): ApiMethods
    {
        val retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com").addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(ApiMethods::class.java)

    }
}