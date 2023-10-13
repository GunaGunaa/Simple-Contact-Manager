package com.example.jetpackcompose.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.navigation.Screen
import com.example.jetpackcompose.repository.CAContactRepository
import com.example.jetpackcompose.viewmodel.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.shimmer
import javax.inject.Inject

class LoginScreen() {
    //    @Inject
//    lateinit var components: Components
    @Composable
    fun SetLogin(
        navController: NavHostController,
        loginViewModel: LoginViewModel = hiltViewModel()
    ) {
        val system = rememberSystemUiController()
        val context = LocalContext.current
        val components = Components()
        // components.AlertDialogComponent(context =context)
        with(loginViewModel.loginUiState) {
            components.FullScreenProgressDialog(isLoading)
            system.setSystemBarsColor(color = MaterialTheme.colors.background)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = stringResource(R.string.ny_image),
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .size(80.dp)
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Card(
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            text = stringResource(R.string.user_name),
                            color = colorResource(id = R.color.steel),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
//                        components.SignupTextField(
//                            value = userName,
//                            onValueChange = {
//                                loginViewModel.setUsername(it)
//                                loginViewModel.loginUiState.userNameError = null
//                                loginViewModel.loginUiState.mailInvalidError = null
//                            },
//                            placeholder = stringResource(R.string.enter_user_name)
//                        )
                        TextField(
                            value = userName,
                            onValueChange = {
                                loginViewModel.setUsername(it)
                                loginViewModel.loginUiState.userNameError = null
                                loginViewModel.loginUiState.mailInvalidError = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 0.dp
                                )
                                .height(50.dp),
                            textStyle = TextStyle(color = MaterialTheme.colors.primary),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey),
                            ),
                            placeholder = { components.AddressText(text = stringResource(R.string.enter_user_name)) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_mail_outline_24),
                                    contentDescription = "mail",modifier = Modifier.padding(top = 3.dp), tint = colorResource(
                                        id = R.color.pink
                                    )
                                )
                            }
                        )
                        if (userNameError != null) {
                            userNameError?.asString()?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption,
                                )
                            }
                        }
                        if (mailInvalidError != null && userName.isNotEmpty()) {
                            mailInvalidError?.asString()?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.error
                                )
                            }
                        }
                    }
                }

                if (loginViewModel.loginUiState.sendEmailDialog) {
                    ForgotPasswordScreen().ForgotPasswordEmailDialog(loginViewModel, navController)
                    // loginViewModel.loginUiState=loginViewModel.loginUiState.copy(sendEmailDialog = false)
                }

                Box(
                    modifier = Modifier.height(6.dp)
                )
                Card() {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            text = stringResource(R.string.password),
                            color = colorResource(id = R.color.steel),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        TextField(
                            value = loginViewModel.loginUiState.password,
                            onValueChange = {
                                loginViewModel.setPassword(it)
                                loginViewModel.loginUiState.passwordError = null
                                loginViewModel.loginUiState.passwordInvalidError = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(
                                    start = 0.dp
                                ),
                            textStyle = TextStyle(color = MaterialTheme.colors.primary),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = colorResource(id = R.color.transparent),
                                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                                backgroundColor = colorResource(id = R.color.lightGrey),
                            ),
                            visualTransformation = if (loginViewModel.passwordVisibility) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image =
                                    if (!loginViewModel.passwordVisibility) R.drawable.ic_claims_download_done else R.drawable.ic_password_hide
                                IconButton(onClick = {
                                    loginViewModel.passwordVisibility =
                                        !loginViewModel.passwordVisibility
                                }) {
                                    Icon(
                                        painter = painterResource(image),
                                        contentDescription = "",
                                    )
                                }
                            },
                            placeholder = { components.AddressText(text = stringResource(R.string.enter_password)) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lock),
                                    contentDescription = "key",tint = colorResource(
                                        id = R.color.pink
                                )
                                )
                            }
                        )

                        if (loginViewModel.loginUiState.passwordError != null) {
                            passwordError?.asString()?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption,
                                )
                            }
                        }
                        if (passwordInvalidError != null && password.isNotEmpty()) {
                            passwordInvalidError?.asString()?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption,
                                )
                            }
                        }
                    }
                }
                if (isErrorMessage != null) {
                    loginViewModel.loginUiState.isErrorMessage?.let {
                        components.AlertDialog(
                            it.asString(), onClick =
                            {})
                    }
                }
                Button(
                    onClick = {
                        loginViewModel.validation()
                    },
                    colors =
                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.pink)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        color = colorResource(id = R.color.white)
                    )
                }
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = colorResource(id = R.color.pink),
                    modifier = Modifier
                        .padding(top = 59.dp)
                        .clickable {
                            loginViewModel.setDialogState()
                        },
                )
                Text(
                    text = stringResource(R.string.sign_up),
                    color = colorResource(id = R.color.pink),
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .clickable {
                            navController.navigate(Screen.SignupScreen.route) {
                                navController.popBackStack()
                            }
                        }
                )
                Text(
                    text = stringResource(R.string.version),
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.steel),
                    modifier = Modifier.padding(top = 51.dp),
                )
                loginViewModel.loginUiState.let {
                    LaunchedEffect(it) {
                        if (it.isLoginSuccess) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "userData",loginViewModel.loginUiState.userData
                            )
                            navController.navigate(Screen.ContactListScreen.route)
                        }
                        if (it.validation) {
                            loginViewModel.loginValidation()
                        }
                    }
                }
            }
        }
    }
}