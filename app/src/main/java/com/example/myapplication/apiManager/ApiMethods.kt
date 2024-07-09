package com.example.myapplication.apiManager

import com.example.myapplication.apiManager.model.User
import com.example.myapplication.apiManager.model.UserResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiMethods
{
    @GET("/users")
    fun getUserList(@Query("limit") limitValue: Int, @Query("skip") skipValue: Int):Call<UserResponseData>


    @GET("/users/{userID}")
    fun getUserDetailsByID(@Path("userID") userID:Int):Call<User>
}