package com.example.jetpackcompose.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.viewmodel.ChangePasswordViewModel

class CAPasswordChangeScreen {
    @Composable
    fun SetChangePassword(
        navController: NavHostController,
        viewModel: ChangePasswordViewModel = hiltViewModel()
    ) {
        val components = Components()
        val context = LocalContext.current
        components.FullScreenProgressDialog(isOpen = viewModel.changePasswordUiState.isLoading)
        Column {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .align(Alignment.CenterStart)
                            .clickable { navController.popBackStack() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                        Text(
                            text = "Back",
                            color = colorResource(id = R.color.pink),
                            fontSize = 17.sp
                        )
                    }

                    Text(
                        text = "Reset Password",
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, modifier = Modifier.align(Alignment.Center)
                    )
                    Text(
                        text = "Done",
                        color = colorResource(id = R.color.pink),
                        modifier = Modifier
                            .padding(end = 17.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                viewModel.validation()
                            },
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(Modifier.padding(start = 15.dp, end = 15.dp)) {
                components.Card {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "Current Password")
                        TextField(
                            value = viewModel.changePasswordUiState.currentPassword,
                            onValueChange = {
                                viewModel.setCurrentPassword(it)
                                viewModel.changePasswordUiState.currentPasswordError = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp), textStyle = TextStyle(
                                color =
                                MaterialTheme.colors.primary
                            ), colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            )
                        )
                        if (viewModel.changePasswordUiState.currentPasswordError.isNotEmpty()) {
                            components.ErrorText(text = viewModel.changePasswordUiState.currentPasswordError)
                        }
                    }
                }
                components.Card {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "New Password")
                        TextField(
                            value = viewModel.changePasswordUiState.password,
                            onValueChange = {
                                viewModel.setNewPassword(it)
                                viewModel.changePasswordUiState.newPasswordError = ""
                                viewModel.changePasswordUiState.validPassword = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp), textStyle = TextStyle(
                                color =
                                MaterialTheme.colors.primary
                            ), colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            )
                        )
                        if (viewModel.changePasswordUiState.newPasswordError.isNotEmpty()) {
                            components.ErrorText(text = viewModel.changePasswordUiState.newPasswordError)
                        }
                        if (viewModel.changePasswordUiState.newPasswordError.isNotEmpty() && viewModel.changePasswordUiState.validPassword.isNotEmpty()) {
                            components.ErrorText(text = viewModel.changePasswordUiState.validPassword)
                        }
                    }

                }
                components.Card {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "Confirm Password")
                        TextField(
                            value = viewModel.changePasswordUiState.confirmPassword,
                            onValueChange = {
                                viewModel.setConfirmPassword(it)
                                viewModel.changePasswordUiState.confirmPasswordError = ""
                                viewModel.changePasswordUiState.passwordMatching = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp), textStyle = TextStyle(
                                color =
                                MaterialTheme.colors.primary
                            ), colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey)
                            )
                        )
                        if (viewModel.changePasswordUiState.confirmPasswordError.isNotEmpty()) {
                            components.ErrorText(text = viewModel.changePasswordUiState.confirmPasswordError)
                        }
                        if (viewModel.changePasswordUiState.confirmPasswordError.isNotEmpty() && viewModel.changePasswordUiState.passwordMatching.isNotEmpty()) {
                            components.ErrorText(text = viewModel.changePasswordUiState.passwordMatching)
                        }
                    }
                }
            }
        }
        if (viewModel.changePasswordUiState.fieldValidation) {
            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
            viewModel.resetPassword()
        }
    }
}