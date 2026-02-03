package com.tripmateapp.utilidades

import android.content.Context

class UserSessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
    }

    fun setUserId(userId: Int) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, 0)
    }

    fun clear() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }
}
