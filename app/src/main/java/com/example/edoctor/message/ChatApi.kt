package com.example.edoctor.message

import com.example.edoctor.API.ChatItem
import com.example.edoctor.API.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("/chat/send")
    suspend fun sendMessage(@Body message: Message): Response<Unit>

    @GET("chat/{user1}/{user2}")
    suspend fun getMessages(
        @Path("user1") user1: String,
        @Path("user2") user2: String
    ): List<Message>
    @GET("api/chats/doctor/{doctorId}")
    suspend fun getDoctorChats(@Path("doctorId") doctorId: String): Response<List<ChatItem>>


}
