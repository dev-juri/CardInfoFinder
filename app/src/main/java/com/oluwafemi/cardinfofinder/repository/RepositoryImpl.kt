package com.oluwafemi.cardinfofinder.repository

import android.util.Log
import com.oluwafemi.cardinfofinder.util.asDomainModel
import com.oluwafemi.cardinfofinder.domain.CardDetails
import com.oluwafemi.cardinfofinder.network.CardDetailsAPI
import com.oluwafemi.cardinfofinder.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl : Repository {

    override suspend fun getCardDetails(url: Long): Result<CardDetails> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = CardDetailsAPI.retrofitService.fetchCardDetailsAsync(url).await()
            Log.i("REQUEST", request.toString())
           Result.Success(request.asDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}