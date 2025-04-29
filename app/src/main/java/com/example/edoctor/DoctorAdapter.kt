package com.example.edoctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.w3c.dom.Text

class DoctorAdapter(var doctors: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvDoctorType: TextView = itemView.findViewById(R.id.tvDoctorType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctor_type, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.tvDoctorName.text = "${doctor.firstName} ${doctor.lastName}"
        holder.tvDoctorType.text = doctor.specialization
    }

    override fun getItemCount(): Int {
        return doctors.size
        }

    fun updateData(newDoctors: List<Doctor>) {
        doctors = newDoctors
        notifyDataSetChanged()
    }
}
