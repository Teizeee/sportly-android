package com.simple.sportly.ui.screen.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onLastNameChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onPatronymicChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    val pageBackground = Color(0xFFF4F2EC)
    val fieldBackground = Color(0xFFD6CFBF)
    val primaryButton = Color(0xFF59584D)
    val fieldText = Color(0xFF8E8A7C)
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .padding(horizontal = 28.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        TextField(
            value = state.lastName,
            onValueChange = onLastNameChange,
            placeholder = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = state.firstName,
            onValueChange = onFirstNameChange,
            placeholder = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = state.patronymic,
            onValueChange = onPatronymicChange,
            placeholder = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Пароль") },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Outlined.VisibilityOff
                        } else {
                            Icons.Outlined.Visibility
                        },
                        contentDescription = "Показать пароль",
                        tint = Color.Black
                    )
                }
            },
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = { Text("Подтвердите пароль") },
            visualTransformation = if (isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) {
                            Icons.Outlined.VisibilityOff
                        } else {
                            Icons.Outlined.Visibility
                        },
                        contentDescription = "Показать пароль",
                        tint = Color.Black
                    )
                }
            },
            colors = textFieldColors(fieldBackground, fieldText)
        )
        Spacer(modifier = Modifier.height(30.dp))

        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(0.76f),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = primaryButton)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
            } else {
                Text(
                    "Зарегистрироваться",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Serif,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Войти",
                color = primaryButton,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun textFieldColors(fieldBackground: Color, fieldText: Color) = TextFieldDefaults.colors(
    focusedContainerColor = fieldBackground,
    unfocusedContainerColor = fieldBackground,
    disabledContainerColor = fieldBackground,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedPlaceholderColor = fieldText,
    unfocusedPlaceholderColor = fieldText
)
