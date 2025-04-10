package com.dinajpur.app.screen.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dinajpur.app.DataClass.RegisterRequest
import com.dinajpur.app.DataClass.RegisterResponse
import com.dinajpur.app.R
import com.dinajpur.app.databinding.ActivityRegistrationBinding
import com.dinajpur.app.network.ApiClient
import com.dinajpur.app.screen.auth.model.Location
import com.dinajpur.app.screen.auth.model.RegistrationRequest
import com.dinajpur.app.utils.Result
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    private var locations: List<Location> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()


    }

    private fun setupUI() {
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spGender.adapter = genderAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.tvLogin.setOnClickListener {
            // Navigate to LoginActivity (uncomment and implement if needed)
            // startActivity(Intent(this, LoginActivity::class.java))
        }
    }



    private fun setupLocationSpinner(locations: List<Location>) {
        val locationNames = listOf("Select Location") + locations.map { it.location_name }
        val locationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            locationNames
        )
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLocation.adapter = locationAdapter
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val passwordConfirm = binding.etPasswordConfirm.text.toString().trim()
        val gender = binding.spGender.selectedItem.toString()
        val locationPosition = binding.spLocation.selectedItemPosition

        when {
            name.isEmpty() -> showError("Please enter your name")
            mobile.isEmpty() -> showError("Please enter your mobile number")
            email.isEmpty() -> showError("Please enter your email")
            password.isEmpty() -> showError("Please enter a password")
            password != passwordConfirm -> showError("Passwords do not match")
            gender == "Select Gender" -> showError("Please select a gender")
            locationPosition == 0 -> showError("Please select a location")
            else -> {
                val locationId = if (locations.isNotEmpty() && locationPosition > 0) {
                    locations[locationPosition - 1].id
                } else {
                    1 // Default fallback
                }

//                val request = RegistrationRequest(
//                    name = name,
//                    mobile = mobile,
//                    email = email,
//                    password = password,
//                    password_confirmation = passwordConfirm,
//                    gender = gender.lowercase(),
//                    location_id = locationId // Fixed: Use calculated locationId
//                )


                val request = RegisterRequest(
                    name = name,
                    mobile = mobile,
                    email = email,
                    password = password,
                    password_confirmation = passwordConfirm,
                    gender = gender.lowercase(),
                    location_id = locationId // Fixed: Use calculated locationId
                )

                ApiClient.apiService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {
                                Log.d("API_SUCCESS", "Message: ${it.message}, OTP: ${it.verifyCode}")
                                // এখানে OTP ভেরিফিকেশনের স্ক্রিনে নিয়ে যেতে পারো
                            }
                        } else {
                            Log.e("API_ERROR", "Response failed: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.e("API_FAILURE", "Error: ${t.localizedMessage}")
                    }
                })

            }
        }





    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}