package com.example.edoctor.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.edoctor.API.ApiClient
import com.example.edoctor.API.AppointmentResponse
import com.example.edoctor.API.UserDataResponse
import com.example.edoctor.R
import com.example.edoctor.activitys.ChatActivity
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var doctorName: String = ""
    private var doctorId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "Created onViewCreated")


        loadUserData()
        loadAppointment()
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

    private fun updateUI(userData: UserDataResponse?) {
        view?.findViewById<TextView>(R.id.tvUserName2)?.text = userData?.login ?: "Гость"
        view?.findViewById<TextView>(R.id.tvMoneyValue)?.text = "${userData?.value ?: '0'} Руб"

        val chatSection = view?.findViewById<ShapeableImageView>(R.id.imageViewInfoBar)
        chatSection?.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("doctorName", doctorName)
            intent.putExtra("doctorId", doctorId)
            intent.putExtra("userLogin", userData?.login ?: "guest")
            startActivity(intent)
        }
    }

    private fun updateAppointmentUI(appointment: AppointmentResponse?) {
        Log.d("HomeFragment", "Updating UI with: $appointment")
        if (appointment != null) {
            doctorName = "${appointment.doctorFirstName} ${appointment.doctorSecondName}"
            doctorId = appointment.doctorId
            val formattedDate = appointment.dateTime.replace("T", " ")
            Log.d("HomeFragment", "Updating UI with: $appointment")
            view?.findViewById<TextView>(R.id.tvDoctorName)?.text = doctorName
            view?.findViewById<TextView>(R.id.tvDoctorQualify)?.text = appointment.doctorSpecialization ?: "Неизвестно"
            view?.findViewById<TextView>(R.id.tvDate)?.text = formattedDate
        }
    }


    private fun loadAppointment() {
        Log.d("HomeFragment", "loadAppointment() called")

        val token = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            Log.e("HomeFragment", "Token is null")
            return
        }

        Log.d("HomeFragment", "Token found: $token")

        ApiClient.authApi.getNextAppointment("Bearer $token").enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                Log.d("HomeFragment", "Appointment response received: ${response.code()}")
                if (response.isSuccessful) {
                    val appointment = response.body()
                    Log.d("HomeFragment", "Appointment loaded: $appointment")
                    updateAppointmentUI(appointment)
                } else {
                    Log.e("HomeFragment", "Appointment request failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.e("HomeFragment", "Appointment error: ${t.message}")
            }
        })
    }

}