package com.example.edoctor.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.AuthResponse
import com.example.edoctor.API.LoginRequest
import com.example.edoctor.R
import com.example.edoctor.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.textViewTitle.setOnClickListener {
            val intent = Intent(this, DoctorLoginActivity::class.java)
            startActivity(intent)
        }

        binding.textViewRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener{
            try {
                loginUser(binding.editTextLogin.text.toString(), binding.editTextPassword.text.toString())
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(login: String, password: String) {
        val call = ApiClient.authApi.login(LoginRequest(login, password))
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    // Сохраняем только логин
                    saveUserLogin(login)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user_login", login)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Ошибка входа!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
                println(t.message)
            }

            private fun saveUserLogin(login: String) {
                val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                prefs.edit()
                    .putString("user_login", login)
                    .apply()
            }
        })
    }

}