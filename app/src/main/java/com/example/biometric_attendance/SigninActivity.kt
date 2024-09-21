package com.example.biometric_attendance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.biometric_attendance.Database.DatabaseHandler


class SigninActivity : AppCompatActivity() {
    private lateinit var toRegisterScreenText: TextView
    private lateinit var signinBtn: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        val context = this


        signinBtn = findViewById(R.id.signinBtn)

        signinBtn.setOnClickListener{

            emailInput = findViewById(R.id.emailInputSignin)
            passwordInput = findViewById(R.id.passwordInputSignin)
            if (emailInput.text.toString().isNotEmpty() && passwordInput.text.toString()
                    .isNotEmpty()
            ){
                val db = DatabaseHandler(context)
                val returnedUserId: Int = db.returnUserId(emailInput.text.toString(), passwordInput.text.toString())

                if (returnedUserId > 0){
                    val intent = Intent(this, HomeActivity::class.java)
                        .putExtra("userId", returnedUserId.toString())
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please fill all data", Toast.LENGTH_SHORT).show()
            }
        }



        toRegisterScreenText = findViewById(R.id.toRegisterScreenText)

        toRegisterScreenText.setOnClickListener{

            startActivity(Intent(this, SignupActivity::class.java))
        }


    }
}