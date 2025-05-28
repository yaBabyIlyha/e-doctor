package com.example.edoctor.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.edoctor.App
import com.example.edoctor.R
import com.google.android.material.switchmaterial.SwitchMaterial

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
    }
}