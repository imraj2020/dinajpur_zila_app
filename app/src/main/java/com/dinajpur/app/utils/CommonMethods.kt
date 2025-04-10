package com.dinajpur.app.utils

import android.accounts.NetworkErrorException
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dinajpur.app.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class CommonMethods(private val context: Context) {

    var sessionManager: SessionManager = SessionManager(context)

    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting)) {
            Toast.makeText(context, "You are offline", Toast.LENGTH_LONG).show()
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

//    fun showMatDialog(
//        title: String?,
//        message: String?
//    ) {
//        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
//            .setTitle(title)
//            .setMessage(message)
//            .setPositiveButton(
//                "পুনরায় চেষ্টা করুন"
//            ) { dialogInterface, i -> }
//
//            .show()
//    }


    fun showCustomDialog(activity: Activity, title: String, bodyText: String): AlertDialog? {
        if (activity.isFinishing || activity.isDestroyed) {
            Log.w("CommonMethods", "Activity is finishing or destroyed, skipping dialog")
            return null
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.custom_alert_dialog, null)
        val titleText = view.findViewById<TextView>(R.id.titleText)
        val contentBody = view.findViewById<TextView>(R.id.contentBodyId)
        val btnClose = view.findViewById<MaterialButton>(R.id.btnCloseId)

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setView(view)
        titleText.text = title
        contentBody.text = bodyText

        val dialog = dialogBuilder.create()

        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            val params = attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        return dialog
    }

     fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun exceptionHandler(throwable: Throwable): String {
            return when (throwable) {
                is NetworkErrorException ->
                    "ডিভাইসটি মোটেও নেটওয়ার্কের সাথে সংযুক্ত নয়"

                is SocketTimeoutException ->
                    "অনুরোধের সময়সীমা শেষ"

                is UnknownHostException ->
                    "সার্ভার ত্রুটি"

                is HttpException ->
                    "কোনো কারণে নেটওয়ার্ক ব্যর্থ হয়েছে আবার চেষ্টা করুন"

                else -> "অভ্যন্তরীণ ত্রুটি আমরা এই সমস্যার সমাধান করছি ${throwable.message}"
            }




        }


        fun dateFormatter(dateTime: String): String {
            var thisDate = ""
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val outputFormat = SimpleDateFormat("dd MMM yy")
                val date = inputFormat.parse(dateTime)
                thisDate = outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return thisDate
        }

        fun getCurrentDate(): String? {
            val currentDate = SimpleDateFormat("ddmmss")
            val todayDate = Date()
            return currentDate.format(todayDate)
        }




    }

    fun dateFormatter(dateTime: String): String {
        var thisDate = ""
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd MMM yy")
            val date = inputFormat.parse(dateTime)
            thisDate = outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return thisDate
    }



    private fun setDatePicker(dateEditText: TextInputEditText) {

        val myCalendar = Calendar.getInstance()

        val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = (year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth)
            dateEditText.setText(date)
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(context, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }


    fun showDatePickerDialog() {
        // Get current date as default
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date (month is 0-based, so add 1 for display)
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                Toast.makeText(context, "Date Selected: $formattedDate", Toast.LENGTH_SHORT).show()
                // Add your logic here (e.g., filter data based on date)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    enum class DialogType {
        SUCCESS, ERROR, WARNING, INFO
    }


}