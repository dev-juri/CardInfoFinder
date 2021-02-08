package com.oluwafemi.cardinfofinder.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.oluwafemi.cardinfofinder.domain.CardDetails
import com.oluwafemi.cardinfofinder.network.NetworkResponse.NetworkResponse

/*
* Map the network response to the domain model
*/
fun NetworkResponse.asDomainModel(): CardDetails {
    return CardDetails(
        bankName = this.bank.name,
        cardName = this.scheme,
        countryName = this.country.name,
        cardType = this.type
    )
}

/*Check if phone is connected*/
@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
    } else {
        val activeNetworlkInfo = connectivityManager.activeNetwork
        if (activeNetworlkInfo != null) {
            return true
        }

    }
    return false
}
