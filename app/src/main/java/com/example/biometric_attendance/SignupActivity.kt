package com.example.biometric_attendance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast


class SignupActivity : AppCompatActivity() {
    private lateinit var signupBtn: Button
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var toSigninScreenText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val context = this


        signupBtn = findViewById(R.id.signupBtn)

        signupBtn.setOnClickListener {
            usernameInput = findViewById(R.id.usernameInput)
            emailInput = findViewById(R.id.emailInput)
            if (usernameInput.text.toString().length > 0 && emailInput.text.toString().length > 0) {
                if (isEmailValid(emailInput.text.toString())) {
                    //got to set password page
                    val intent = Intent(this, SetPasswordActivity::class.java)
                        .putExtra("username", usernameInput.text.toString())
                        .putExtra("email", emailInput.text.toString())

                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill all data", Toast.LENGTH_SHORT).show()
            }
        }

        toSigninScreenText = findViewById(R.id.toSigninScreenText)

        toSigninScreenText.setOnClickListener {

            startActivity(Intent(this, SigninActivity::class.java))
        }

    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

