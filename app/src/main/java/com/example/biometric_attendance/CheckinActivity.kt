package com.example.biometric_attendance

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.biometric_attendance.Database.DatabaseHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class CheckinActivity : AppCompatActivity() {
    private lateinit var textUserIdCheckin: TextView
    private lateinit var checkinCompletedText: TextView
    private lateinit var checkinCompleteBtn: Button
    private lateinit var toHomeScreenCheckinWindow: TextView

    private lateinit var checkinWarning: TextView

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)
        val context = this

        textUserIdCheckin = findViewById(R.id.userIdCheckin)
        val userId = intent.getStringExtra("userId").toString()

        textUserIdCheckin.text = "User ID: " + userId
        checkinCompleteBtn = findViewById(R.id.checkinCompleteBtn)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        checkinCompleteBtn.setOnClickListener {

            val db = DatabaseHandler(context)
            if (db.statusToday(userId, getDate(), "checkin")) {
                checkinWarning = findViewById(R.id.checkinWarning)
                checkinWarning.text = "You've already checked in for today."
                checkinCompleteBtn.setEnabled(false)

            } else {

                if (getLocationPermission()) {
                    val location = fusedLocationProviderClient.lastLocation

                    location.addOnSuccessListener {
                        if (it != null) {
                            val currentLocation =
                                it.latitude.toString() + " " + it.longitude.toString()

                            if (officeLocation == currentLocation) {

                                Toast.makeText(
                                    this,
                                    "Location matched with the office premises",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()


                                val result = db.insertCheckin(userId, getDate())

                                if (result != -1.toLong()) {
                                    checkinCompletedText = findViewById(R.id.checkinCompletedText)
                                    checkinCompletedText.text =
                                        "Check-in completed at " + getCurrentTime()
                                    checkinCompleteBtn.setEnabled(false)
                                }

                            } else
                                Toast.makeText(
                                    this,
                                    "You are not at the office premises",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error accessing location",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }


                    location.addOnFailureListener {
                        Toast.makeText(this, "Location access failed", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

        toHomeScreenCheckinWindow = findViewById(R.id.toHomeScreenCheckinWindow)
        toHomeScreenCheckinWindow.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).putExtra("userId", userId))
        }


    }

    private val officeLocation: String = "37.4219983 -122.084"


    private fun getDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return formatter.format(date)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val m = calendar.get(Calendar.MINUTE)
        return h.toString() + ":" + m.toString()
    }


    private fun getLocationPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            false
        } else {
            true
        }

    }

}