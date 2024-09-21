package com.example.biometric_attendance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biometric_attendance.Database.DatabaseHandler

class AttendanceActivity : AppCompatActivity() {

    private lateinit var attendanceAdapter: AttendanceAdapter
    private lateinit var db: DatabaseHandler
    private lateinit var attendanceRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)


        val userId = intent.getStringExtra("userId").toString()

        db = DatabaseHandler(this)

        attendanceAdapter = AttendanceAdapter(db.attendanceRecord(userId), this)

        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView)

        attendanceRecyclerView.layoutManager =
            LinearLayoutManager(this)

        attendanceRecyclerView.adapter = attendanceAdapter
    }


}