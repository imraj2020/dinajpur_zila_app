package com.dinajpur.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    // Existing properties (unchanged for brevity)
    var refreshToken: String?
        get() = sharedPreferences.getString("refresh_token", "")
        set(refresh_token) = sharedPreferences.edit().putString("refresh_token", refresh_token).apply()

    var accessToken: String?
        get() = sharedPreferences.getString("access_token", "")
        set(accessToken) = sharedPreferences.edit().putString("access_token", accessToken).apply()

    var isLogin: Boolean?
        get() = sharedPreferences.getBoolean("is_login", false)
        set(isLogin) = sharedPreferences.edit().putBoolean("is_login", isLogin!!).apply()

    var isShopOwner: Boolean?
        get() = sharedPreferences.getBoolean("is_shop_owner", false)
        set(isShopOwner) = sharedPreferences.edit().putBoolean("is_shop_owner", isShopOwner!!).apply()

    var mobileNumber: String?
        get() = sharedPreferences.getString("mobile_number", "")
        set(mobileNumber) = sharedPreferences.edit().putString("mobile_number", mobileNumber).apply()

    var isForgetPas: Boolean?
        get() = sharedPreferences.getBoolean("is_forget_pas", false)
        set(isForgetPas) = sharedPreferences.edit().putBoolean("is_forget_pas", isForgetPas!!).apply()

    var isProfile: Boolean?
        get() = sharedPreferences.getBoolean("is_profile", false)
        set(isProfile) = sharedPreferences.edit().putBoolean("is_profile", isProfile!!).apply()

    var labelHeight: String?
        get() = sharedPreferences.getString("label_height", "")
        set(labelHeight) = sharedPreferences.edit().putString("label_height", labelHeight).apply()

    var labelWidth: String?
        get() = sharedPreferences.getString("label_width", "")
        set(labelWidth) = sharedPreferences.edit().putString("label_width", labelWidth).apply()

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return isLogin == true
    }

    var selectedShopId: String?
        get() = sharedPreferences.getString(KEY_SHOP_ID, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_SHOP_ID, value).apply()
        }

    var shopName: String?
        get() = sharedPreferences.getString(KEY_SHOP_NAME, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_SHOP_NAME, value).apply()
        }

    var firstName: String?
        get() = sharedPreferences.getString(KEY_FIRST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_FIRST_NAME, value).apply()

    var lastName: String?
        get() = sharedPreferences.getString(KEY_LAST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_LAST_NAME, value).apply()

    var gender: String?
        get() = sharedPreferences.getString(KEY_GENDER, null)
        set(value) = sharedPreferences.edit().putString(KEY_GENDER, value).apply()

    var birthDate: String?
        get() = sharedPreferences.getString(KEY_BIRTH_DATE, null)
        set(value) = sharedPreferences.edit().putString(KEY_BIRTH_DATE, value).apply()

    var profilePicture: String?
        get() = sharedPreferences.getString(KEY_PROFILE_PICTURE, null)
        set(value) = sharedPreferences.edit().putString(KEY_PROFILE_PICTURE, value).apply()

    var isCheckNotifySMS: Boolean?
        get() = sharedPreferences.getBoolean("is_check_notify_sms", false)
        set(isCheckNotifySMS) = sharedPreferences.edit().putBoolean("is_check_notify_sms", isCheckNotifySMS!!).apply()


    companion object {
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SHOP_NAME = "shop_name"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_PROFILE_PICTURE = "profile_picture"
        private const val KEY_GENDER = "gender"
        private const val KEY_BIRTH_DATE = "birth_date"
    }
}