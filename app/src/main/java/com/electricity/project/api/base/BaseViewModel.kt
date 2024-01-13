package com.electricity.project.api.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    private var mJob: Job? = null

    protected fun <T> baseRequest(
        mutableStateFlow: MutableLiveData<ApiResponse<T>>,
        errorHandler: CoroutinesErrorHandler,
        function: suspend () -> Response<T>,
    ) {
        mJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                errorHandler.onError(error.localizedMessage ?: "Error occured! Please try again.")
            }
        }) {
            apiRequestFlow { function.invoke() }.collect {
                withContext(Dispatchers.Main) {
                    mutableStateFlow.value = it
                }
            }
        }
    }

    private fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
        emit(ApiResponse.Loading)

        withTimeoutOrNull(20000L) {
            val response = call()

            try {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        emit(ApiResponse.Success(data))
                    }
                } else {
                    response.let {
                        Log.w(
                            BaseViewModel::class.java.toString(),
                            "Error: ${it.errorBody()?.string()}, Code: ${it.code()}"
                        )
                        emit(ApiResponse.Failure("${it.errorBody()?.string()}", it.code()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
            }
        } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
    }.flowOn(Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}

interface CoroutinesErrorHandler {
    fun onError(message: String)
}