package com.example.edoctor.activitys

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edoctor.API.AppointmentResponse
import com.example.edoctor.R

class HistoryAdapter(private val appointments: List<AppointmentResponse>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val doctorType: TextView = itemView.findViewById(R.id.tvDoctorType)
        val time: TextView = itemView.findViewById(R.id.tvTime)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val doctorImage: ImageView = itemView.findViewById(R.id.ivDoctor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_layout, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.doctorName.text = "${appointment.doctorFirstName} ${appointment.doctorSecondName}"
        holder.doctorType.text = appointment.doctorSpecialization
        val dateTime = appointment.dateTime.split("T")
        holder.date.text = dateTime.getOrNull(0) ?: ""
        holder.time.text = dateTime.getOrNull(1)?.substring(0, 5) ?: ""
        // Можно добавить загрузку изображения
    }

    override fun getItemCount(): Int = appointments.size
}
