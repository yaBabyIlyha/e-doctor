package com.example.edoctor.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.AppointmentResponse
import com.example.edoctor.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private val appointmentList = mutableListOf<AppointmentResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        val login = this.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("user_login", null) ?: return

        adapter = HistoryAdapter(appointmentList) { appointment ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("doctorId", appointment.doctorId)
                putExtra("doctorName", "${appointment.doctorFirstName} ${appointment.doctorSecondName}")
                putExtra("userLogin", login)
                putExtra("isDoctor", false)
            }
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.rvDoctorHistory)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ApiClient.authApi.getAllAppointmentsByLogin(login).enqueue(object :
            Callback<List<AppointmentResponse>> {
            override fun onResponse(call: Call<List<AppointmentResponse>>, response: Response<List<AppointmentResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        appointmentList.clear()
                        appointmentList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<AppointmentResponse>>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, "Ошибка загрузки истории", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

