package com.oluwafemi.cardinfofinder.util

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