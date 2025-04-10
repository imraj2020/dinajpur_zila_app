package com.dinajpur.app.screen

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dinajpur.app.R
import com.dinajpur.app.databinding.ActivityWelcomePageBinding
import com.dinajpur.app.screen.auth.LoginActivity
import com.dinajpur.app.screen.auth.RegistrationActivity

class WelcomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomePageBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.signInBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            finish()
        }
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}