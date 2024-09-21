package com.example.biometric_attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biometric_attendance.model.AttendanceObject

class AttendanceAdapter(private var attendance: List<AttendanceObject>, context: Context) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {
    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTextView: TextView = itemView.findViewById(R.id.dateCardview)
        val checkinTextView: TextView = itemView.findViewById(R.id.checkinCardview)
        val checkoutTextView: TextView = itemView.findViewById(R.id.checkoutCardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.attendance_item, parent, false)
        return AttendanceViewHolder(view)

    }

    override fun getItemCount(): Int = attendance.size

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendanceVal = attendance[position]

        holder.dateTextView.text = "Date: " + attendanceVal.date
        if (attendanceVal.checkinTime != null) holder.checkinTextView.text =
            "Check-in: " + attendanceVal.checkinTime
        if (attendanceVal.checkoutTime != null) holder.checkoutTextView.text =
            "Check-out: " + attendanceVal.checkoutTime

    }

}