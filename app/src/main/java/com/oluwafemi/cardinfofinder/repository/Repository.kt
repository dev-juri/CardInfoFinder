package com.oluwafemi.cardinfofinder.repository

import com.oluwafemi.cardinfofinder.domain.CardDetails
import com.oluwafemi.cardinfofinder.util.Result


interface Repository {
    suspend fun getCardDetails (url: Long) : Result<CardDetails>
}