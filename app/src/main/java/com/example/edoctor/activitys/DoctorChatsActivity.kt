package com.example.edoctor.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.ChatItem
import com.example.edoctor.R
import com.example.edoctor.message.ChatAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorChatsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_chats)

        recyclerView = findViewById(R.id.recyclerDoctorChats)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализируем адаптер пустым списком и сразу назначаем на RecyclerView
        adapter = ChatAdapter(emptyList()) { userLogin ->
            val doctorId = intent.getStringExtra("doctorId") ?: return@ChatAdapter
            val intent = Intent(this@DoctorChatsActivity, ChatActivity::class.java)
            intent.putExtra("doctorId", doctorId)
            intent.putExtra("userLogin", userLogin)
            intent.putExtra("isDoctor", true)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        adapter.updateChats(
            listOf(
                ChatItem("TestUser1", "Hello!"),
                ChatItem("TestUser2", "How are you?")
            )
        )

        val doctorId = intent.getStringExtra("doctorId") ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.chatApi.getDoctorChats(doctorId)
                if (response.isSuccessful) {
                    val chats = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        Log.d("DoctorChatsActivity", "Received chats: $chats")
                        adapter.updateChats(chats) // обновляем список в адаптере
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DoctorChatsActivity, "Ошибка загрузки чатов", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
