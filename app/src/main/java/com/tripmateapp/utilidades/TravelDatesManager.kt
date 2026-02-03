package com.tripmateapp.utilidades

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate
import java.time.ZoneId

class TravelDatesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("travel_dates", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_FECHA_INICIO = "fecha_inicio"
        private const val KEY_FECHA_FIN = "fecha_fin"
    }

    fun getFechaInicioMillis(): Long? {
        val ms = prefs.getLong(KEY_FECHA_INICIO, -1L)
        return if (ms == -1L) null else ms
    }

    fun getFechaFinMillis(): Long? {
        val ms = prefs.getLong(KEY_FECHA_FIN, -1L)
        return if (ms == -1L) null else ms
    }
    
    fun saveTravelDates(fechaInicio: Long?, fechaFin: Long?) {
        prefs.edit().apply {
            putLong(KEY_FECHA_INICIO, fechaInicio ?: -1L)
            putLong(KEY_FECHA_FIN, fechaFin ?: -1L)
            apply()
        }
    }
    
    fun getTravelDates(): List<LocalDate> {
        val fechaInicioMs = prefs.getLong(KEY_FECHA_INICIO, -1L)
        val fechaFinMs = prefs.getLong(KEY_FECHA_FIN, -1L)
        
        if (fechaInicioMs == -1L || fechaFinMs == -1L) {
            return emptyList()
        }
        
        val fechaInicio = LocalDate.ofInstant(
            java.time.Instant.ofEpochMilli(fechaInicioMs),
            ZoneId.systemDefault()
        )
        val fechaFin = LocalDate.ofInstant(
            java.time.Instant.ofEpochMilli(fechaFinMs),
            ZoneId.systemDefault()
        )
        
        return generateSequence(fechaInicio) { it.plusDays(1) }
            .takeWhile { !it.isAfter(fechaFin) }
            .toList()
    }
    
    fun clearTravelDates() {
        prefs.edit().clear().apply()
    }
}
