package com.tripmateapp


import java.time.LocalDate

enum class ItineraryType {
    ACTIVIDAD,
    RESTAURANTE,
    TRANSPORTE
}

data class ItineraryItem(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val date: LocalDate,
    val type: ItineraryType
)
