package com.example.edoctor.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.AppointmentRequest
import com.example.edoctor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddSecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_second)

        val doctorName = intent.getStringExtra("doctorName")
        val doctorType = intent.getStringExtra("doctorType")
        val doctorId = intent.getStringExtra("doctorId")
        val userLogin = intent.getStringExtra("userLogin")

        findViewById<TextView>(R.id.tvDoctorName).text = doctorName
        findViewById<TextView>(R.id.tvDoctorType).text = doctorType

        val dateEditText = findViewById<EditText>(R.id.tvChooseDate)
        val timeEditText = findViewById<EditText>(R.id.tvChooseTime)
        val confirmButton = findViewById<Button>(R.id.bSend)
        val exitButton = findViewById<ImageView>(R.id.imClose)

        exitButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        confirmButton.setOnClickListener {
            val dateInput = dateEditText.text.toString()
            val timeInput = timeEditText.text.toString()

            if (dateInput.isBlank() || timeInput.isBlank()) {
                Toast.makeText(this, "Введите дату и время", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                val dateTime = LocalDateTime.parse("$dateInput $timeInput", formatter)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.authApi.createAppointment(
                            AppointmentRequest(
                                doctorId = doctorId ?: "",
                                dateTime = dateTime.toString(), // ISO 8601 формат
                                userLogin = userLogin ?: ""
                            )
                        )

                        runOnUiThread {
                            Toast.makeText(this@AddSecondActivity, "Запись успешна!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddSecondActivity, MainActivity::class.java))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this@AddSecondActivity, "Ошибка при записи: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Неверный формат даты или времени", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
