package com.dinajpur.app.screen.auth

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dinajpur.app.MainActivity
import com.dinajpur.app.R
import com.dinajpur.app.databinding.ActivityLoginBinding
import com.dinajpur.app.network.ApiClient
import com.dinajpur.app.screen.auth.model.LoginRequest
import com.dinajpur.app.screen.auth.model.LoginResponse
import com.dinajpur.app.utils.CommonMethods
import com.dinajpur.app.utils.CommonProgressBar
import com.dinajpur.app.utils.ErrorResponse
import com.dinajpur.app.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var commonMethods: CommonMethods
    lateinit var sessionManager: SessionManager
    private lateinit var commonProgressBar: CommonProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Failed to initialize UI: ${e.message}")
            showMessageDialog(
                activity = this,
                title = "Error",
                message = "UI initialization failed",
                dialogType = DialogType.ERROR,
                positiveAction = { finish() }
            )
            return
        }

        // Initialize utilities
        commonMethods = CommonMethods(this)
        sessionManager = SessionManager(this)
        commonProgressBar = CommonProgressBar(this)

        // Check if user is already logged in
        if (sessionManager.isLogin == true && sessionManager.accessToken!!.isNotEmpty()) {
            redirectToMainActivity()
            return // Exit onCreate to prevent further UI setup
        }

        // Clear session only if not logged in (optional, depending on your needs)
        try {
            sessionManager.clearSession()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Failed to clear session: ${e.message}")
        }

        // Initialize TextWatcher for Phone Number
        binding.edtPhone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = s.toString()
                when {
                    phoneNumber.isEmpty() -> {
                        binding.edtPhone.isErrorEnabled = false
                        binding.edtPhone.error = null
                    }
                    phoneNumber.length != 11 -> {
                        binding.edtPhone.isErrorEnabled = true
                        binding.edtPhone.error = "Mobile Number must be 11 digits"
                    }
                    !isValidMobileNo(phoneNumber) -> {
                        binding.edtPhone.isErrorEnabled = true
                        binding.edtPhone.error = "Give a valid Mobile Number"
                    }
                    else -> {
                        binding.edtPhone.isErrorEnabled = false
                        binding.edtPhone.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }) ?: Log.e("LoginActivity", "edtPhone editText is null")

        // Initialize TextWatcher for Password
        binding.edtPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when {
                    password.isEmpty() -> {
                        binding.edtPassword.isErrorEnabled = false
                        binding.edtPassword.error = null
                    }
//                    password.length < 8 -> {
//                        binding.edtPassword.isErrorEnabled = true
//                        binding.edtPassword.error = "Password must be at least 8 characters"
//                    }
                    else -> {
                        binding.edtPassword.isErrorEnabled = false
                        binding.edtPassword.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }) ?: Log.e("LoginActivity", "edtPassword editText is null")

        // Login Button Click Listener
        binding.btnLogin.setOnClickListener {
            val mobileNumber = binding.edtPhone.editText?.text.toString().trim() ?: ""
            val password = binding.edtPassword.editText?.text.toString().trim() ?: ""

            var isValid = true

            // Mobile Number Validation
            when {
                mobileNumber.isEmpty() -> {
                    binding.edtPhone.isErrorEnabled = true
                    binding.edtPhone.error = "Enter a Mobile Number"
                    isValid = false
                }
                mobileNumber.length != 11 -> {
                    binding.edtPhone.isErrorEnabled = true
                    binding.edtPhone.error = "Mobile Number must be 11 digits"
                    isValid = false
                }
                !isValidMobileNo(mobileNumber) -> {
                    binding.edtPhone.isErrorEnabled = true
                    binding.edtPhone.error = "Enter a valid Mobile Number"
                    isValid = false
                }
                else -> {
                    binding.edtPhone.isErrorEnabled = false
                    binding.edtPhone.error = null
                }
            }

            // Password Validation
            when {
                password.isEmpty() -> {
                    binding.edtPassword.isErrorEnabled = true
                    binding.edtPassword.error = "Enter a Password"
                    isValid = false
                }
//                password.length < 8 -> {
//                    binding.edtPassword.isErrorEnabled = true
//                    binding.edtPassword.error = "Password must be at least 8 characters"
//                    isValid = false
//                }
                else -> {
                    binding.edtPassword.isErrorEnabled = false
                    binding.edtPassword.error = null
                }
            }

            if (!isValid || isFinishing || isDestroyed) return@setOnClickListener

            // Proceed with login only if inputs are valid
            val model = LoginRequest("$mobileNumber", password)
            if (commonMethods.isOnline()) {
                try {
                    if (!isFinishing && !isDestroyed) commonProgressBar.show()
                    login(model)
                    sessionManager.mobileNumber = "88$mobileNumber"
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Login initiation failed: ${e.message}")
                    showMessageDialog(
                        activity = this,
                        title = "Error",
                        message = "Login failed unexpectedly",
                        dialogType = DialogType.ERROR
                    )
                }
            } else {
                showMessageDialog(
                    activity = this,
                    title = "Connection Error",
                    message = "Internet Connection Error",
                    dialogType = DialogType.WARNING
                )
            }
        }
    }

    private fun redirectToMainActivity() {
        if (!isFinishing && !isDestroyed) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun login(loginReq: LoginRequest) {
        val apiService = ApiClient.apiService

        apiService.login(loginReq).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (isFinishing || isDestroyed) return
                commonProgressBar.hide()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("API_CALL", "Request: $loginReq")
                    loginResponse?.let {
                        if (response.code() == 200) {
                            sessionManager.isLogin = true

                            // Update SessionManager
                            sessionManager.accessToken = "Bearer ${it.access_token}"


                            // Use null-safe message with fallback
                            val message = it.message ?: "Login Successful"
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                            redirectToMainActivity()
                        } else {
                            val errorMessage = when (response.code()) {
                                500 -> "Internal Server Error!"

                                else -> "Login Failed!"
                            }
                            showMessageDialog(
                                activity = this@LoginActivity,
                                title = "Error",
                                message = errorMessage,
                                dialogType = DialogType.ERROR
                            )
                        }
                    }
                } else {
                    val errorMessage = if (response.code() == 400) {
                        "User not found! Please register first."
                    } else {
                        response.errorBody()?.string()?.let { errorBody ->
                            try {
                                Gson().fromJson(errorBody, ErrorResponse::class.java)?.errorMessage
                                    ?: "Unknown Error"
                            } catch (e: Exception) {
                                Log.e("LoginActivity", "Error parsing errorBody: ${e.message}")
                                "Invalid server response"
                            }
                        } ?: "Unknown Error"
                    }
                    runOnUiThread {
                        if (!isFinishing && !isDestroyed) {
                            showMessageDialog(
                                activity = this@LoginActivity,
                                title = "Error",
                                message = errorMessage,
                                dialogType = DialogType.ERROR
                            )
                        }
                    }
                    Log.d("API_CALL", "Response: ${response.code()} - $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (isFinishing || isDestroyed) return
                commonProgressBar.hide()
                val errorMessage = t.message ?: "Network Error"
                showMessageDialog(
                    activity = this@LoginActivity,
                    title = "Network Error",
                    message = "Error: $errorMessage",
                    dialogType = DialogType.ERROR,
                    positiveButtonText = "Retry",
                    negativeButtonText = "Cancel",
                    positiveAction = { login(loginReq) } // Retry login
                )
            }
        })
    }

    private fun isValidMobileNo(bdNumberStr: String?): Boolean {
        if (bdNumberStr == null) return false
        val phoneUtil = PhoneNumberUtil.getInstance()
        val isValid: Boolean = try {
            val bdNumberProto = phoneUtil.parse(bdNumberStr, "BD")
            phoneUtil.isValidNumber(bdNumberProto)
        } catch (e: NumberParseException) {
            Log.e("LoginActivity", "NumberParseException was thrown: ${e.message}")
            false
        } catch (e: Exception) {
            Log.e("LoginActivity", "Unexpected error in phone validation: ${e.message}")
            false
        }
        return isValid
    }

    enum class DialogType {
        SUCCESS, ERROR, WARNING, INFO
    }

    private fun showMessageDialog(
        activity: Activity,
        title: String,
        message: String,
        dialogType: DialogType = DialogType.INFO,
        positiveButtonText: String = "OK",
        negativeButtonText: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeAction: (() -> Unit)? = null,
        autoDismissAfter: Long? = null // Time in milliseconds, null for no auto-dismiss
    ) {
        if (activity.isFinishing || activity.isDestroyed) return

        val dialogBuilder = MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false) // Prevents dismissal by tapping outside

        // Set icon based on dialog type
        val icon: Drawable? = when (dialogType) {
            DialogType.SUCCESS -> ContextCompat.getDrawable(activity, R.drawable.ic_success)
            DialogType.ERROR -> ContextCompat.getDrawable(activity, R.drawable.ic_error)
            DialogType.WARNING -> ContextCompat.getDrawable(activity, R.drawable.ic_warning)
            DialogType.INFO -> ContextCompat.getDrawable(activity, R.drawable.ic_info)
        }
        icon?.let { dialogBuilder.setIcon(it) }

        // Customize positive button
        dialogBuilder.setPositiveButton(positiveButtonText) { dialog, _ ->
            positiveAction?.invoke()
            dialog.dismiss()
        }

        // Optional negative button
        negativeButtonText?.let { text ->
            dialogBuilder.setNegativeButton(text) { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            }
        }

        // Create and show the dialog
        val dialog = dialogBuilder.create()

        // Apply custom styling
        dialog.setOnShowListener {
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)?.let { button ->
                button.setTextColor(ContextCompat.getColor(activity, R.color.dialog_button_positive))
            }
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)?.let { button ->
                button.setTextColor(ContextCompat.getColor(activity, R.color.dialog_button_negative))
            }
        }

        dialog.show()

        // Auto-dismiss if specified
        autoDismissAfter?.let { delay ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, delay)
        }
    }
}