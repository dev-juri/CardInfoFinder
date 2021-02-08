package com.oluwafemi.cardinfofinder.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.oluwafemi.cardinfofinder.domain.CardDetails
import com.oluwafemi.cardinfofinder.repository.RepositoryImpl
import com.oluwafemi.cardinfofinder.util.Result
import kotlinx.coroutines.launch

/*
* CLass for state management for the UI to observe
* */
enum class DataState {
    SUCCESS, ERROR, LOADING
}

class MainActivityViewModel(private val repository: RepositoryImpl) : ViewModel() {

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState>
        get() = _dataState

    private val _cardDetails = MutableLiveData<CardDetails>()
    val cardDetails: LiveData<CardDetails>
        get() = _cardDetails


    /*accepts a userInput @param or type Long, and pass it to the getCardDetails func from the Repository*/
    fun fetchDetails(userInput: Long) {
        _dataState.value = DataState.LOADING
        viewModelScope.launch {
            when (val result = repository.getCardDetails(userInput)) {
                is Result.Success -> {
                    _cardDetails.value = result.data
                    _dataState.value = DataState.SUCCESS
                    Log.i("RESULT", result.data.toString())
                }
                is Error -> {
                    _dataState.value = DataState.ERROR
                    Log.e("RESULT", result.message.toString())
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(val repository: RepositoryImpl) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            (MainActivityViewModel(repository) as T)
    }
}