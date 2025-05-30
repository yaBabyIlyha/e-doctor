package com.example.edoctor.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.edoctor.App
import com.example.edoctor.R
import com.example.edoctor.activitys.HistoryActivity
import com.example.edoctor.activitys.LoginActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import org.w3c.dom.Text

class ProfileFragment : Fragment() {
    private lateinit var mySwitch: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userLogin = prefs.getString("user_login", null)

        view.findViewById<TextView>(R.id.tvUserName2).text = userLogin

        var logOutButton = view.findViewById<ImageView>(R.id.imageViewSettingsIcon)
        mySwitch = view.findViewById(R.id.themeSwitcher)

        (activity?.application as? App)?.let { app ->
            mySwitch.isChecked = app.darkTheme

            mySwitch.setOnCheckedChangeListener { _, isChecked ->
                app.switchTheme(isChecked)
            }
        } ?: run {
            mySwitch.isEnabled = false
            Log.e("ProfileFragment", "Failed to get App instance")
        }

        logOutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<TextView>(R.id.tvHistory).setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}