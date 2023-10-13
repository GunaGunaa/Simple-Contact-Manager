package com.example.jetpackcompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.navigation.Screen
import com.example.jetpackcompose.viewmodel.SignupViewModel

class SignupScreen {
    @Composable
    fun SetSignup(
        navController: NavHostController,
        viewModel: SignupViewModel = hiltViewModel()
    ) {
        val context = LocalContext.current
        val components = Components()
        with(viewModel.signupUiState) {
            components.FullScreenProgressDialog(isOpen = isLoading)
            LaunchedEffect(validationSuccess) {
                if (validationSuccess) {
                    viewModel.userSignUp()
                    viewModel.signupUiState =
                        viewModel.signupUiState.copy(validationSuccess = false)
                }
            }
            if (responseSuccess.isNotEmpty()) {
                components.AlertDialog(message = viewModel.signupUiState.responseSuccess) {
                    viewModel.signupUiState = viewModel.signupUiState.copy(otpDialog = true)
                    viewModel.signupUiState = viewModel.signupUiState.copy(responseSuccess = "")
                }
            }
            if (responseFailure != null) {
                responseFailure?.let {
                    components.AlertDialog(
                        message = it.asString(),
                        onClick = {
                            viewModel.signupUiState =
                                viewModel.signupUiState.copy(responseFailure = null)
                        })
                }
            }
            if (otpDialog) {
                SignUpOTPDialog().SignUpOTPDialog(navController, viewModel)
            }


            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
            ) {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = colorResource(id = R.color.pink),
                            modifier = Modifier
                                .padding(start = 17.dp)
                                .align(Alignment.CenterStart)
                                .clickable {
                                    navController.navigate(Screen.LoginScreen.route)
                                },
                            fontSize = 17.sp
                        )
                        Text(
                            text = stringResource(R.string.signup),
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = stringResource(R.string.save),
                            color = colorResource(id = R.color.pink),
                            modifier = Modifier
                                .padding(end = 17.dp)
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    viewModel.signUpValidation()
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 19.dp, end = 19.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                components.Text(text = stringResource(R.string.first_name))
                                components.SignupTextField(
                                    value = firstName,
                                    onValueChange = {
                                        viewModel.setFirstName(it)
                                        viewModel.signupUiState.firstNameError = null
                                    })
                                if (firstNameError != null) {
                                    firstNameError?.let { components.ErrorText(text = it.asString()) }
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                components.Text(text = stringResource(R.string.last_name))
                                components.SignupTextField(
                                    value = lastName,
                                    onValueChange = {
                                        viewModel.setLastName(it)
                                        viewModel.signupUiState.lastNameError = null
                                    })
                                if (lastNameError != null
                                ) lastNameError?.let { components.ErrorText(text = it.asString()) }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    components.Card {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(R.string.company))
                            components.SignupTextField(value = company, onValueChange = {
                                viewModel.setCompany(it)
                                viewModel.signupUiState.companyError = null
                            })
                            if (companyError != null) companyError?.let { components.ErrorText(text = it.asString()) }
                        }
                    }

                    components.Card {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(R.string.email))
                            components.SignupTextField(value = email, onValueChange = {
                                viewModel.setMail(it)
                                viewModel.signupUiState.emailError = null
                                viewModel.signupUiState.emailFormatError = null
                            })
                            if (emailError != null) emailError?.let { components.ErrorText(text = it.asString()) }
                            if (email.isNotEmpty() && emailFormatError != null) emailFormatError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }
                        }
                    }
                    components.Card {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(R.string.phone))
                            components.SignupTextField(
                                value = phone,
                                onValueChange = {
                                    viewModel.setPhone(it)
                                    viewModel.signupUiState.phoneError = null
                                    viewModel.signupUiState.numberFormatError = null
                                },
                            )
                            if (phoneError != null) phoneError?.let { components.ErrorText(text = it.asString()) }
                            if (numberFormatError != null && phone.isNotEmpty()) numberFormatError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }
                        }
                    }
                    components.Card {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(id = R.string.password))
                            components.SignupTextField(value = password, onValueChange = {
                                viewModel.setPassword(it)
                                viewModel.signupUiState.passwordError = null
                                viewModel.signupUiState.passwordFormatError = null
                            })
                            if (passwordError != null) passwordError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }
                            if (password.isNotEmpty() && passwordFormatError != null) passwordFormatError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }

                        }
                    }
                    components.Card {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(R.string.confirm_pasword))
                            components.SignupTextField(
                                value = confirmPassword,
                                onValueChange = {
                                    viewModel.setConfirmPassword(it)
                                    viewModel.signupUiState.confirmPasswordError = null
                                    viewModel.signupUiState.passwordMatchingError = null
                                })
                            if (confirmPasswordError != null) confirmPasswordError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }
                            if (confirmPassword.isNotEmpty() && passwordMatchingError != null) passwordMatchingError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }

                        }
                    }
                    components.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 9.dp)
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            components.Text(text = stringResource(R.string.address))
                            components.SignupTextField(
                                value = street,
                                onValueChange = {
                                    viewModel.setStreetName(it)
                                    viewModel.signupUiState.streetError = null
                                },
                                placeholder = stringResource(R.string.street)
                            )
                            if (streetError != null) streetError?.let { components.ErrorText(text = it.asString()) }
                            Spacer(modifier = Modifier.height(6.dp))
                            components.SignupTextField(
                                value = city,
                                onValueChange = {
                                    viewModel.setCityName(it)
                                    viewModel.signupUiState.cityError = null
                                },
                                placeholder = stringResource(R.string.city)
                            )
                            if (cityError != null) cityError?.let { components.ErrorText(text = it.asString()) }
                            Spacer(modifier = Modifier.height(6.dp))
                            components.SignupTextField(
                                value = state,
                                onValueChange = {
                                    viewModel.setStateName(it)
                                    viewModel.signupUiState.stateError = null
                                },
                                placeholder = stringResource(R.string.state)
                            )
                            if (stateError != null) stateError?.let { components.ErrorText(text = it.asString()) }
                            Spacer(modifier = Modifier.height(6.dp))
                            components.SignupTextField(
                                value = postalCode,
                                onValueChange = {
                                    viewModel.setPostalCode(it)
                                    viewModel.signupUiState.postalCodeError = null
                                },
                                placeholder = stringResource(R.string.postal_code)
                            )
                            if (postalCodeError != null) postalCodeError?.let {
                                components.ErrorText(
                                    text = it.asString()
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}


