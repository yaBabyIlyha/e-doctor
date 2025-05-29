package com.example.edoctor.message

import com.example.edoctor.API.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("/chat/send")
    suspend fun sendMessage(@Body message: Message): Response<Unit>

    @GET("/chat/{login}/{doctorId}")
    suspend fun getMessages(
        @Path("login") login: String,
        @Path("doctorId") doctorId: String
    ): List<Message>
}
