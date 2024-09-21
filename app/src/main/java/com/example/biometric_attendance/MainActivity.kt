package com.example.biometric_attendance

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.biometric_attendance.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        /*
val context = this

        val usernameInput = binding.usernameInput.text.toString()
        val emailInput = binding.emailInput.text.toString()
        val passwordInput = "password"


binding.signupBtn.setOnClickListener({
    if (usernameInput.length > 0 && emailInput.length > 0 && passwordInput.length > 0){
var user = User(usernameInput, emailInput, passwordInput)
        var db = DatabaseHandler(context)
        db.insertData(user)

    } else {
        Toast.makeText(context, "Please fill all data", Toast.LENGTH_SHORT).show()
    }

})

*/
    }
}