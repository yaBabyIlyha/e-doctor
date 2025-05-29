package com.example.edoctor.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.R
import com.example.edoctor.databinding.DoctorTypeBinding

class DoctorAdapter(
    private var doctors: List<Doctor>,
    private val onItemClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    fun updateData(newDoctors: List<Doctor>) {
        this.doctors = newDoctors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = DoctorTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctors[position])
    }

    override fun getItemCount(): Int = doctors.size

    inner class DoctorViewHolder(private val binding: DoctorTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.tvDoctorName.text = "${doctor.firstName} ${doctor.secondName}"
            binding.tvDoctorType.text = doctor.specialization
            binding.root.setOnClickListener {
                onItemClick(doctor)
            }
        }
    }
}
