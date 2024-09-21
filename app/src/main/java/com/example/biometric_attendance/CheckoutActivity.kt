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


class CheckoutActivity : AppCompatActivity() {
    private lateinit var textUserIdCheckout: TextView
    private lateinit var checkoutCompleteBtn: Button

    private lateinit var checkoutWarning: TextView
    private lateinit var checkoutCompletedText: TextView
    private lateinit var toHomeScreenCheckoutWindow: TextView

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val context = this

        textUserIdCheckout = findViewById(R.id.userIdCheckout)
        val userId = intent.getStringExtra("userId").toString()

        textUserIdCheckout.text = "User ID: $userId"


        checkoutCompleteBtn = findViewById(R.id.checkoutCompleteBtn)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        checkoutCompleteBtn.setOnClickListener {

            val db = DatabaseHandler(context)


            if (!db.statusToday(userId, getDate(), "checkin")) {
                checkoutWarning = findViewById(R.id.checkoutWarning)
                checkoutWarning.text = "You have not checked in today."
                checkoutCompleteBtn.setEnabled(false)

            } else if (db.statusToday(userId, getDate(), "checkout")) {

                checkoutWarning = findViewById(R.id.checkoutWarning)
                checkoutWarning.text = "You've already checked out today."
                checkoutCompleteBtn.setEnabled(false)
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


                                val result = db.insertCheckout(userId, getDate())

                                if (result) {
                                    checkoutCompletedText = findViewById(R.id.checkoutCompletedText)
                                    checkoutCompletedText.text =
                                        "Check-out completed at " + getCurrentTime()
                                    checkoutCompleteBtn.setEnabled(false)
                                }

                            } else
                                Toast.makeText(
                                    this,
                                    "You are not at the office premises",
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

        toHomeScreenCheckoutWindow = findViewById(R.id.toHomeScreenCheckoutWindow)
        toHomeScreenCheckoutWindow.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).putExtra("userId", userId))
        }

    }


    private val officeLocation: String = "37.4219983 -122.084"

    private fun getLocation(): Boolean {
        // check location permission
        if (ActivityCompat.checkSelfPermission(
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

        }

        var isAtOffice: Boolean = false
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                val currentLocation = it.latitude.toString() + " " + it.longitude.toString()
                if (officeLocation == currentLocation) {
                    isAtOffice = true
                    Toast.makeText(
                        this,
                        "Location matched with the office premises",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else
                    Toast.makeText(this, "You are not at the office premises", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        location.addOnFailureListener {
            Toast.makeText(this, "Location access failed", Toast.LENGTH_SHORT).show()
        }

        return isAtOffice

    }


    private fun getDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return formatter.format(date)
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val m = calendar.get(Calendar.MINUTE)
        return h.toString() + ":" + m.toString()
    }


    fun getLocationPermission(): Boolean {
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