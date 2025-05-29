package com.example.edoctor.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

class ChatActivity : AppCompatActivity() {

    private lateinit var doctorId: String
    private lateinit var userLogin: String
    private lateinit var messageAdapter: MessageAdapter
    private var isDoctor: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Получаем данные из Intent
        doctorId = intent.getStringExtra("doctorId") ?: return
        userLogin = intent.getStringExtra("userLogin") ?: "guest"
        val doctorName = intent.getStringExtra("doctorName") ?: "Доктор"
        isDoctor = intent.getBooleanExtra("isDoctor", false)

        // Устанавливаем имя доктора (можно поменять, если пользователь)
        findViewById<TextView>(R.id.tvDoctorChatName).text =
            if (isDoctor) userLogin else doctorName

        // Настройка RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerMessages)
        messageAdapter = MessageAdapter(if (isDoctor) doctorId else userLogin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        // Загрузка сообщений
        loadMessages()

        // Кнопка отправки
        findViewById<ImageButton>(R.id.btnSend).setOnClickListener {
            val messageText = findViewById<EditText>(R.id.etMessage).text.toString()
            if (messageText.isNotBlank()) {
                sendMessage(messageText)
                findViewById<EditText>(R.id.etMessage).setText("")
            }
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
        val senderId = if (isDoctor) doctorId else userLogin
        val receiverId = if (isDoctor) userLogin else doctorId

        val message = Message(
            senderLogin = senderId,
            receiverLogin = receiverId,
            content = content
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
