package com.tripmateapp.utilidades

import android.content.Context
import android.content.SharedPreferences

class SelectedDestinationManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("selected_destination", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_DESTINO_ID = "destino_id"
    }
    
    fun saveSelectedDestination(destinoId: Int) {
        prefs.edit().putInt(KEY_DESTINO_ID, destinoId).apply()
    }
    
    fun getSelectedDestination(): Int {
        return prefs.getInt(KEY_DESTINO_ID, 1) // Default to 1 if not set
    }
    
    fun clearSelectedDestination() {
        prefs.edit().remove(KEY_DESTINO_ID).apply()
    }
}
