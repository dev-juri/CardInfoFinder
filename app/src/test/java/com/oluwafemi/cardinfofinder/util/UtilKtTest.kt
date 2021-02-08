package com.oluwafemi.cardinfofinder.util

import com.oluwafemi.cardinfofinder.domain.CardDetails
import com.oluwafemi.cardinfofinder.network.NetworkResponse.Bank
import com.oluwafemi.cardinfofinder.network.NetworkResponse.Country
import com.oluwafemi.cardinfofinder.network.NetworkResponse.NetworkResponse
import com.oluwafemi.cardinfofinder.network.NetworkResponse.Number
import org.junit.Assert
import org.junit.Test

class UtilKtTest {

    @Test
    fun asDomainModel_Test() {
        //GIVEN
        val networkResponse = NetworkResponse(
            Bank(
                "Ibadan",
                "GTBANK",
                "08168036131",
                "www.gtbank.com"
            ),
            "Debit",
            Country(
                "",
                "NGN",
                "NG",
                9,
                8,
                "NIGERIA",
                ""
            ),
            Number(16, false),
            false,
            "MasterCard",
            "Debit"
        )
        //WHEN .asDomainModel is called on the object
        val cardDetails = networkResponse.asDomainModel()

        //THEN we expect
        val expectedResult = CardDetails(
            "GTBANK",
            "MasterCard",
            "NIGERIA",
            "Debit"
        )

        Assert.assertEquals(expectedResult, cardDetails)
    }
}