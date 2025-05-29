package com.example.edoctor.activitys

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.Message
import com.example.edoctor.R
import com.example.edoctor.message.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ChatActivity : AppCompatActivity() {

    private lateinit var doctorId: String
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var userLogin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Получение данных из Intent
        userLogin = intent.getStringExtra("userLogin") ?: "guest"
        doctorId = intent.getStringExtra("doctorId") ?: return
        val doctorName = intent.getStringExtra("doctorName") ?: "Доктор"

        // Установка имени доктора
        findViewById<TextView>(R.id.tvDoctorChatName).text = doctorName

        // Настройка RecyclerView и адаптера
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerMessages)
        messageAdapter = MessageAdapter(userLogin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        // Загрузка сообщений
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val messages = ApiClient.chatApi.getMessages(userLogin, doctorId)
                withContext(Dispatchers.Main) {
                    messageAdapter.setMessages(messages)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Кнопка отправки сообщений
        findViewById<ImageButton>(R.id.btnSend).setOnClickListener {
            val messageText = findViewById<EditText>(R.id.etMessage).text.toString()
            if (messageText.isNotBlank()) {
                sendMessage(messageText)
                findViewById<EditText>(R.id.etMessage).setText("")
            }
        }

        loadMessages()
    }

    private fun loadMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val messages = ApiClient.chatApi.getMessages(userLogin, doctorId)
                withContext(Dispatchers.Main) {
                    messageAdapter.setMessages(messages)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendMessage(content: String) {
        val message = Message(
            sender = userLogin,
            receiverDoctorId = doctorId,
            content = content,
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.chatApi.sendMessage(message)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        messageAdapter.addMessage(message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
