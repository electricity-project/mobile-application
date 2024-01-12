package com.electricity.project.api.authorization.viewmodel

import com.electricity.project.api.authorization.entity.LoginRequestDTO
import com.electricity.project.api.authorization.entity.LoginResponseDTO
import com.electricity.project.api.authorization.service.AuthorizationService
import com.electricity.project.api.base.ApiResponse
import com.electricity.project.api.base.BaseViewModel
import com.electricity.project.api.base.CoroutinesErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthorizationViewModel(
    private val authorizationService: AuthorizationService
) : BaseViewModel() {

    private val _authorizationData =
        MutableStateFlow<ApiResponse<LoginResponseDTO>>(ApiResponse.Empty)

    val authorizationData: StateFlow<ApiResponse<LoginResponseDTO>> = _authorizationData

    fun login(requestDTO: LoginRequestDTO, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(_authorizationData, coroutinesErrorHandler) {
            authorizationService.login(requestDTO)
        }


}