package com.example.notes.domain.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {
    fun convertTimestampToFormattedDate(timestamp: Long): String {
        // Create a date object from the timestamp
        val date = Date(timestamp)

        // Define the desired date format
        val dateFormat = SimpleDateFormat("d MMMM yyyy 'at' HH:mm", Locale.getDefault())

        // Format the date and return it
        return dateFormat.format(date)
    }
}