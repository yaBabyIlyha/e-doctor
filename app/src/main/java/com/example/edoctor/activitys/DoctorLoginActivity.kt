package com.example.edoctor.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.DoctorLoginRequest
import com.example.edoctor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorLoginActivity : AppCompatActivity() {

    private lateinit var etDoctorId: EditText
    private lateinit var etDoctorPassword: EditText
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_login)

        etDoctorId = findViewById(R.id.etDoctorId)
        etDoctorPassword = findViewById(R.id.etDoctorPassword)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        btnLogin = findViewById(R.id.btnDoctorLogin)

        btnLogin.setOnClickListener {
            val id = etDoctorId.text.toString().trim()
            val password = etDoctorPassword.text.toString().trim()

            if (id.isEmpty() || password.isEmpty()) {
                tvErrorMessage.text = "Введите ID и пароль"
                tvErrorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvErrorMessage.visibility = View.GONE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiClient.authApi.doctorLogin(DoctorLoginRequest(id, password))
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            // Переходим в чат врача, передаем doctorId
                            val intent = Intent(this@DoctorLoginActivity, DoctorChatsActivity::class.java)
                            intent.putExtra("doctorId", id)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this@DoctorLoginActivity, "Sucsesfull Login", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            tvErrorMessage.text = "Неверный ID или пароль"
                            tvErrorMessage.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvErrorMessage.text = "Ошибка сети"
                        tvErrorMessage.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
