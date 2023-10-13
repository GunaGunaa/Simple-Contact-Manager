package com.example.jetpackcompose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.ui.theme.backgroundGrey
import com.example.jetpackcompose.viewmodel.SignupViewModel

class SignUpOTPDialog {
    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("NotConstructor")
    @Composable
    fun SignUpOTPDialog(navController: NavHostController, viewModel: SignupViewModel) {
        var openDialog by remember {
            mutableStateOf(true)
        }
        with(viewModel.signupUiState) {
            LaunchedEffect(signupOtpValidation) {
                if (signupOtpValidation) {
                    viewModel.sendSignUpOTP()
                    viewModel.signupUiState =
                        viewModel.signupUiState.copy(signupOtpValidation = false)
                }
            }
            if (signUpOtpSuccessResponse.isNotEmpty()) {
                openDialog = false
                Components().AlertDialog(
                    message = viewModel.signupUiState.signUpOtpSuccessResponse,
                    onClick = {
                        viewModel.signupUiState = viewModel.signupUiState.copy(otpDialog = false)
                    })

            }
            if (signupOtpFailureResponse != null) {
                viewModel.signupUiState.signupOtpFailureResponse?.let {
                    Components().AlertDialog(
                        message = it.asString(),
                        onClick = {})
                }
            }
            if (openDialog) {
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
                        Column {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 20.dp,
                                    bottom = 5.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.verification),
                                    style = TextStyle(fontSize = 17.sp, color = Color.Black)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.enter_received_otp),
                                    style = TextStyle(fontSize = 13.sp, color = Color.Black)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                TextField(
                                    value = signupOTP,
                                    onValueChange = {
                                        viewModel.setSignupOTP(it)
                                        viewModel.signupUiState.signupOTPError = null
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = colorResource(id = R.color.transparent),
                                        unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                        backgroundColor = colorResource(id = R.color.lightGrey)
                                    ),
                                    placeholder = { Text(text = "OTP") }
                                )
                                if (signupOTPError != null) {
                                    Text(
                                        text = signupOTPError!!.asString(),
                                        color = MaterialTheme.colors.error,
                                        style = MaterialTheme.typography.caption,
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth(), color = colorResource(
                                    id = R.color.divider
                                )
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Cancel", style = TextStyle(
                                            fontSize = 17.sp, color = colorResource(
                                                id = R.color.pink,
                                            )
                                        ), modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .padding(start = 45.dp)
                                            .clickable {
                                                openDialog = false
                                            }
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .fillMaxHeight()
                                            .align(Alignment.Center), color = colorResource(
                                            id = R.color.divider
                                        )
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
                                            .padding(end = 55.dp)
                                            .clickable {
                                                viewModel.otpValidation()
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}