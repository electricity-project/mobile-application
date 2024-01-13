package com.electricity.project.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.electricity.project.R
import com.electricity.project.api.authorization.entity.LoginRequestDTO
import com.electricity.project.api.authorization.viewmodel.AuthorizationViewModel
import com.electricity.project.api.token.TokenViewModel
import com.electricity.project.ui.theme.LogoBlue
import com.electricity.project.ui.theme.LogoBlueBackground

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavHostController,
    authorizationViewModel: AuthorizationViewModel,
    tokenViewModel: TokenViewModel
) {
    MoveToMainView(tokenViewModel, navController)

    val keyboardController = LocalSoftwareKeyboardController.current

    var login by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Surface(
        modifier = modifier,
        color = LogoBlueBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Application logo"
            )
            AccountTextField(
                login, R.string.loginScreen_login,
                KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            ) { login = it }
            AccountTextField(
                password,
                R.string.loginScreen_password,
                KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        authorizationViewModel.login(
                            LoginRequestDTO(
                                login.trimEnd(),
                                password.trimEnd()
                            )
                        )
                    }
                ),
                PasswordVisualTransformation()
            ) { password = it }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = LogoBlue),
                shape = ShapeDefaults.Medium,
                onClick = {
                    authorizationViewModel.login(
                        LoginRequestDTO(
                            login.trimEnd(),
                            password.trimEnd()
                        )
                    )
                }) {
                Text(
                    stringResource(R.string.loginScreen_sign_in),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun MoveToMainView(
    tokenViewModel: TokenViewModel,
    navController: NavHostController
) {
    tokenViewModel.token.observe(LocalLifecycleOwner.current) {
        it?.let {
            navController.navigate(AppScreen.MainView.route)
        }
    }
}

@Composable
private fun AccountTextField(
    parameter: String,
    placeholderId: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = parameter,
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(text = stringResource(placeholderId))
        },
        onValueChange = onValueChange,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.White,
            unfocusedPlaceholderColor = Color.Gray,
            unfocusedTextColor = Color.White,
            focusedBorderColor = LogoBlue,
            focusedPlaceholderColor = LogoBlue,
            focusedTextColor = LogoBlue
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}
