package com.tripmateapp.utilidades

import android.content.Context

class ActiveTripManager(context: Context) {
    private val prefs = context.getSharedPreferences("active_trip", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACTIVE_VIAJE_ID = "active_viaje_id"
    }

    fun setActiveViajeId(viajeId: Int) {
        prefs.edit().putInt(KEY_ACTIVE_VIAJE_ID, viajeId).apply()
    }

    fun getActiveViajeId(): Int {
        return prefs.getInt(KEY_ACTIVE_VIAJE_ID, 0)
    }

    fun clear() {
        prefs.edit().remove(KEY_ACTIVE_VIAJE_ID).apply()
    }
}
