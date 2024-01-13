package com.electricity.project.api.authorization.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.electricity.project.api.authorization.entity.LoginRequestDTO
import com.electricity.project.api.authorization.entity.LoginResponseDTO
import com.electricity.project.api.authorization.service.AuthorizationService
import com.electricity.project.api.base.ApiResponse
import com.electricity.project.api.base.BaseViewModel
import com.electricity.project.api.base.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val authorizationService: AuthorizationService
) : BaseViewModel() {

    private val _authorizationData =
        MutableLiveData<ApiResponse<LoginResponseDTO>>(ApiResponse.Empty)

    val authorizationData = _authorizationData

    fun login(requestDTO: LoginRequestDTO) =
        baseRequest(_authorizationData, object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.w("AuthorizationViewModel", "Error $message")
            }
        }) {
            authorizationService.login(requestDTO)
        }


}