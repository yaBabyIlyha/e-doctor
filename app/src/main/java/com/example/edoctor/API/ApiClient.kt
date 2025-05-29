package com.example.edoctor.API

import com.example.edoctor.activitys.ChatActivity
import com.example.edoctor.message.ChatApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val doctorApi: ApiService = retrofit.create(ApiService::class.java)
    val chatApi: ChatApi = retrofit.create(ChatApi::class.java)
}