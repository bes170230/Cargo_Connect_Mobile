package com.example.cargoconnect

// AuthManager.kt
import android.content.Context

class AuthManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove("JWT_TOKEN").apply()
    }
}
