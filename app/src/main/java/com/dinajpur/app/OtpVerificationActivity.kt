package com.dinajpur.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dinajpur.app.databinding.ActivityOtpVerificationBinding
import com.dinajpur.app.databinding.ActivityRegistrationBinding
import com.dinajpur.app.screen.auth.LoginActivity
import com.dinajpur.app.screen.auth.RegistrationActivity

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val myotp = intent.getStringExtra("Otp")


        binding.btnVerify.setOnClickListener {
            if (binding.OtpEditText.text.toString().equals(myotp)){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                Toast.makeText(this,"Invalid Otp", Toast.LENGTH_LONG).show()
            }

        }


        //




    }
}