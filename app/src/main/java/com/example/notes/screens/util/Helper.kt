package com.example.notes.screens.util

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {
    fun Context.getActivity(): AppCompatActivity? =
        when (this) {
            is AppCompatActivity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                when {
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }

            else -> {
                // use Deprecated method only on older devices
                val activeNetwork = connectivityManager.activeNetworkInfo ?: return false
                return when (activeNetwork.type) {
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_VPN -> true
                    else -> false
                }
            }
        }
    }

    fun convertTimestampToFormattedDate(timestamp: Long): String {
        // Create a date object from the timestamp
        val date = Date(timestamp)

        // Define the desired date format
        val dateFormat = SimpleDateFormat("d MMMM yyyy 'at' HH:mm", Locale.getDefault())

        // Format the date and return it
        return dateFormat.format(date)
    }
}
