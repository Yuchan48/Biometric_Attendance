package com.example.biometric_attendance

import android.content.Intent
import androidx.biometric.BiometricPrompt
import android.os.Bundle
import android.provider.Settings
import android.util.Log

import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private lateinit var textUserId: TextView

    private lateinit var checkinBtn: Button
    private lateinit var checkoutBtn: Button
    private lateinit var recordBtn: Button

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val context = this

        textUserId = findViewById(R.id.userIdHome)
        val userId = intent.getStringExtra("userId").toString()

        textUserId.text = "User ID: $userId"

        val REQUEST_CODE = 1

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }
        var operationType: String = ""
        val intent1 = Intent(this, CheckinActivity::class.java)
            .putExtra("userId", userId)
        val intent2 = Intent(this, CheckoutActivity::class.java)
            .putExtra("userId", userId)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    if (operationType == "checkin")
                        startActivity(intent1)
                    else if (operationType == "checkout")
                        startActivity(intent2)
                    else
                        println("Operation not specified")

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        checkinBtn = findViewById(R.id.checkinBtn)

        checkinBtn.setOnClickListener {
            operationType = "checkin"
            biometricPrompt.authenticate(promptInfo)
        }

        checkoutBtn = findViewById(R.id.checkoutBtn)

        checkoutBtn.setOnClickListener {
            operationType = "checkout"
            biometricPrompt.authenticate(promptInfo)
        }



        recordBtn = findViewById(R.id.recordBtn)
        recordBtn.setOnClickListener {
            val intent = Intent(this, AttendanceActivity::class.java)
                .putExtra("userId", userId)
            startActivity(intent)
        }


    }


}