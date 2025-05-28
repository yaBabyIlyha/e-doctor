package com.example.edoctor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.UserDataResponse
import com.example.edoctor.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
    }

    private fun loadUserData() {
        val token = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null) ?: return

        ApiClient.authApi.getUserData("Bearer $token").enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(
                call: Call<UserDataResponse>,
                response: Response<UserDataResponse>
            ) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    updateUI(userData)
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error: ${t.message}")
            }
        })
    }

    private fun updateUI (userData: UserDataResponse?) {
        view?.findViewById<TextView>(R.id.tvUserName2)?.text = userData?.login ?: "Гость"
        view?.findViewById<TextView>(R.id.tvMoneyValue)?.text = "${userData?.value?: '0'} Руб"
    }
}