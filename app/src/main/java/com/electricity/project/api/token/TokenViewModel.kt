package com.electricity.project.api.token

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    val token = MutableLiveData<String?>()
    val jwtToken = MutableLiveData<JWT?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getToken(TokenTypes.AUTHORIZATION_BEARER_TOKEN_KEY).collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                    it?.let { jwtToken.value = JWT(it) }
                }
            }
        }
    }

    fun saveToken(tokenTypes: TokenTypes, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveToken(tokenTypes, token)
        }
    }

    fun deleteToken(tokenTypes: TokenTypes) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteToken(tokenTypes)
        }
    }

    fun clearTokens() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.clearTokens()
        }
    }
}