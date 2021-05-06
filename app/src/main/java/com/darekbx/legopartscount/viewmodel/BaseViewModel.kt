package com.darekbx.legopartscount.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darekbx.legopartscount.BuildConfig
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<Exception>()

    fun clearError() {
        errorState.postValue(null)
    }

    protected fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                loadingState.value = true
                block()
            } catch (error: Exception) {
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
                errorState.postValue(error)
            } finally {
                loadingState.value = false
            }
        }
    }
}
