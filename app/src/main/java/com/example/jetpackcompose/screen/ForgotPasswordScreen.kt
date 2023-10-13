package com.example.jetpackcompose.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.viewmodel.LoginViewModel

class ForgotPasswordScreen {
    @SuppressLint("UnrememberedMutableState", "NotConstructor")
    @Composable
    @OptIn(ExperimentalComposeUiApi::class)
    fun ForgotPasswordEmailDialog(
        loginViewModel: LoginViewModel,
        navController: NavHostController
    ) {
        val context = LocalContext.current
        LaunchedEffect(loginViewModel.loginUiState.forgotPasswordValidation) {
            if (loginViewModel.loginUiState.forgotPasswordValidation) {
                loginViewModel.forgotPassword()
            }
        }
        if (loginViewModel.forgotPasswordUiStates.forgotPasswordOtpDialog) {
            forgotPasswordOtpDialog(navController = navController, loginViewModel)
        }
        if (loginViewModel.loginUiState.forgotPasswordResponse) {
            loginViewModel.loginUiState=loginViewModel.loginUiState.copy(forgotPasswordValidation = false)
            Components().AlertDialog(
                message = "Please Check your email to reset the password",
                onClick = {
                    loginViewModel.setForgotPasswordOtpTrue()
                })
        }
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Card(
                modifier = Modifier
                    .width(270.dp)
                    .background(colorResource(id = R.color.lightGrey)),
                elevation = 10.dp,
                shape = MaterialTheme.shapes.medium

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
                ) {
                    Text(
                        text = "Forgot Password",
                        style = TextStyle(fontSize = 17.sp, color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add registered email ID to receive OTP",
                        style = TextStyle(fontSize = 13.sp, color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = loginViewModel.loginUiState.forgotPasswordEmail,
                        onValueChange = { loginViewModel.setForgotPasswordEmail(it) },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = colorResource(id = R.color.transparent),
                            unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                            backgroundColor = colorResource(id = R.color.lightGrey)
                        )
                    )
                    if (loginViewModel.loginUiState.emailError.isNotEmpty()) {
                        Text(
                            text = loginViewModel.loginUiState.emailError,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Cancel", style = TextStyle(
                                    fontSize = 17.sp, color = colorResource(
                                        id = R.color.pink,
                                    )
                                ), modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 10.dp)
                                    .clickable {
                                        loginViewModel.loginUiState =
                                            loginViewModel.loginUiState.copy(sendEmailDialog = false)
                                    }
                            )
                            Text(
                                text = "Send OTP",
                                style = TextStyle(
                                    fontSize = 17.sp, color = colorResource(
                                        id = R.color.pink,
                                    )
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 10.dp)
                                    .clickable {
                                        loginViewModel.validateEmail()
                                    }
                            )
                        }
                    }

                }
            }
        }
    }


    @SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
    @Composable
    @OptIn(ExperimentalComposeUiApi::class)
    fun forgotPasswordOtpDialog(navController: NavController, viewModel: LoginViewModel) {
        val context = LocalContext.current
            Components().FullScreenProgressDialog(isOpen =viewModel.forgotPasswordUiStates.isLoading)
        if (viewModel.forgotPasswordUiStates.forgotPasswordFieldValidation) {
            Toast.makeText(context, "Details should not be empty", Toast.LENGTH_SHORT).show()
            viewModel.forgotPasswordUiStates.forgotPasswordFieldValidation = false
        }
        if (viewModel.forgotPasswordUiStates.validationSuccess) {
            viewModel.setNewPassword()
        }
        if (viewModel.forgotPasswordUiStates.forgotPasswordSetPasswordResponse){
            Components().AlertDialog(message ="Successfully password changed please login", onClick = {
                viewModel.forgotPasswordUiStates=viewModel.forgotPasswordUiStates.copy(openDialog=false)
            })
        }
        if (viewModel.forgotPasswordUiStates.openDialog)
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(usePlatformDefaultWidth = true)
            ) {
                Card(
                    modifier = Modifier
                        .width(270.dp)
                        .background(colorResource(id = R.color.lightGrey)),
                    elevation = 10.dp,
                    shape = MaterialTheme.shapes.medium

                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    ) {
                        Text(
                            text = "OTP",
                            style = TextStyle(fontSize = 17.sp, color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your otp,new password and confirm password",
                            style = TextStyle(fontSize = 13.sp, color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            value = viewModel.forgotPasswordUiStates.otp,
                            onValueChange = { viewModel.setForgotPasswordOtp(it) },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            ), placeholder = { Text(text = "OTP") }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = viewModel.forgotPasswordUiStates.password,
                            onValueChange = { viewModel.setForgotPassword(it) },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            ), placeholder = { Text(text = "New Password") }
                        )
                        if (viewModel.forgotPasswordUiStates.invalidPassword.isNotEmpty()) {
                            Components().ErrorText(text = viewModel.forgotPasswordUiStates.invalidPassword)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = viewModel.forgotPasswordUiStates.confirmPassword,
                            onValueChange = { viewModel.setForgotConfirmPassword(it) },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            ), placeholder = { Text(text = "Confirm Password") }
                        )
                        if (viewModel.forgotPasswordUiStates.passwordMatching.isNotEmpty()) {
                            Components().ErrorText(text = viewModel.forgotPasswordUiStates.passwordMatching)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "Cancel", style = TextStyle(
                                        fontSize = 17.sp, color = colorResource(
                                            id = R.color.pink,
                                        )
                                    ), modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 10.dp)
                                        .clickable {
                                            viewModel.forgotPasswordUiStates =
                                                viewModel.forgotPasswordUiStates.copy(openDialog = false)
                                            viewModel.loginUiState =
                                                viewModel.loginUiState.copy(forgotPasswordResponse = false)
                                        }
                                )
                                Text(
                                    text = "Ok",
                                    style = TextStyle(
                                        fontSize = 17.sp, color = colorResource(
                                            id = R.color.pink,
                                        )
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 10.dp)
                                        .clickable {
                                            viewModel.forgotPasswordValidation()
                                        }
                                )
                            }
                        }

                    }
                }
            }
    }
}