package com.example.jetpackcompose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.viewmodel.EditScreenViewModel

class CAEditScreen {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun SetEditScreen(
        navController: NavHostController,
        viewModel: EditScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
        user: User?
    ) {
        val components = Components()
        var firstName by remember { mutableStateOf(TextFieldValue(user!!.first_name)) }
        var firstNameError by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf(TextFieldValue(user!!.last_name)) }
        var lastNameError by remember { mutableStateOf("") }
        var company by remember { mutableStateOf(TextFieldValue("ABC Private Limited")) }
        var companyError by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf(TextFieldValue(user!!.phone_number)) }
        var address by remember { mutableStateOf(TextFieldValue()) }
        Scaffold(topBar = {
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
                                navController.popBackStack()
                            },
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Save",
                        color = colorResource(id = R.color.pink),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 17.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                Row {
                    Card(
                        modifier = Modifier
                            .size(width = 114.dp, height = 212.dp)
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 53.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img),
                                contentDescription = "Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Edit", style = TextStyle(
                                    fontSize = 11.sp, color = colorResource(
                                        id = R.color.pink
                                    )
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(2f)) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                components.Text(text = "First Name")
                                components.SignupTextField(value = firstName, onValueChange = {
                                    firstName = it
                                    firstNameError = ""
                                })
                                if (firstNameError.isNotEmpty()) components.ErrorText(text = firstNameError)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                components.Text(text = "LastName")
                                components.SignupTextField(value = lastName, onValueChange = {
                                    lastName = it
                                    lastNameError = ""
                                })
                                if (lastNameError.isNotEmpty()) components.ErrorText(text = lastNameError)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "Company")
                        components.SignupTextField(
                            value = company,
                            onValueChange = {
                                company = it
                                companyError = ""
                            },
                        )
                        if (companyError.isNotEmpty()) components.ErrorText(text = companyError)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "Phone")
                        components.SignupTextField(value = phone, onValueChange = {
                            phone = it

                        }, leadingIcon = {
                            Text(
                                text = "+91",
                                color = colorResource(id = R.color.pink),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        })
//                        if (phoneError.isNotEmpty()) components.ErrorText(text = phoneError)
//                        if (mobileNumberState && phone.text.isNotEmpty()) components.ErrorText(text = " Enter valid mobile number")
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        components.Text(text = "Address")
                        components.SignupTextField(value = address,
                            onValueChange = {
                                address = it
                            },
                            placeholder = { components.AddressText(text = "ABC Private Limited") })
                        Spacer(modifier = Modifier.height(6.dp))
                        components.SignupTextField(value = address,
                            onValueChange = {
                                address
                            },
                            placeholder = { components.AddressText(text = "ABC Private Limited") })
                        Spacer(modifier = Modifier.height(6.dp))
                        components.SignupTextField(value = address,
                            onValueChange = {
                                viewModel.editScreenUiState.street = it.text
                            },
                            placeholder = { components.AddressText(text = "ABC Private Limited") })
                        Spacer(modifier = Modifier.height(6.dp))
                        components.SignupTextField(value = address,
                            onValueChange = {
                                viewModel.editScreenUiState.street = it.text
                            },
                            placeholder = { components.AddressText(text = "ABC Private Limited") })
                    }
                }
            }
        }
    }
}