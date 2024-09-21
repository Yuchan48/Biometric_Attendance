package com.example.biometric_attendance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import com.example.biometric_attendance.Database.DatabaseHandler
import com.example.biometric_attendance.model.User


class SetPasswordActivity : AppCompatActivity() {
    private lateinit var setPasswordBtn: Button
    private lateinit var textUsername: TextView
    private lateinit var textEmail: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_set_password)

        val context = this

        textUsername = findViewById(R.id.setPasswordUsername)
        textEmail = findViewById(R.id.setPasswordEmail)


        val username = intent.getStringExtra("username").toString()
        val email = intent.getStringExtra("email").toString()


        textUsername.text = "Username: " + username
        textEmail.text = "email: " + email


        setPasswordBtn = findViewById(R.id.setPasswordBtn)

        setPasswordBtn.setOnClickListener{
            val passwordInput1 = findViewById<EditText>(R.id.setPassword1).text.toString()
            val passwordInput2 = findViewById<EditText>(R.id.setPassword2).text.toString()

            if (passwordInput1.length < 8) {
                Toast.makeText(
                    context,
                    "Passwords must be more than 8 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (passwordInput1 != passwordInput2) {
                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(username, email, passwordInput1)
                val db = DatabaseHandler(context)
                val insertedRowId = db.insertUser(user)
                if (insertedRowId != -1L){
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }



    }


}

