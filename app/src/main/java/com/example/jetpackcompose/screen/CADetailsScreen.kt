package com.example.jetpackcompose.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.CAApplication
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.navigation.Screen
import com.example.jetpackcompose.viewmodel.DetailsScreenViewModel

class CADetailsScreen {
    @Composable
    fun SetDetailsScreen(
        navController: NavHostController,
        result: User?,
        viewModel: DetailsScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {
        Toast.makeText(CAApplication.contextApplication, "${result?.first_name}", Toast.LENGTH_SHORT).show()
        val components = Components()
        viewModel.setUser(user = result)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
        ) {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )

                    Text(
                        text = "Contacts",
                        style = TextStyle(
                            color = colorResource(id = R.color.pink),
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (viewModel.detailsScreenUiState.user?.email == CAApplication.sharedPreferences.getUserId()
                    )   {
                        Text(
                            text = "Edit",
                            style = TextStyle(
                                color = colorResource(id = R.color.pink),
                                fontSize = 17.sp
                            ), modifier = Modifier
                                .padding(end = 17.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "user", result
                                    )
                                    navController.navigate(Screen.EditScreen.route)
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
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
                Spacer(modifier = Modifier.height(5.dp))
                viewModel.detailsScreenUiState.user?:viewModel.detailsScreenUiState.userData?.let {
                    Text(
                        text = it.first_name + " " + it.last_name,
                        style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.primary)
                    )
                    Text(
                        text = "ABC Private Limited", style = TextStyle(
                            fontSize = 11.sp, color = colorResource(
                                id = R.color.steel
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    components.Card(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 15.dp,
                                bottom = 15.dp
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.phone), style = TextStyle(
                                    fontSize = 14.sp, color = colorResource(
                                        id = R.color.black
                                    )
                                )
                            )
                            Text(
                                text = "+91 " + it.phone_number, style = TextStyle(
                                    fontSize = 18.sp, color = colorResource(
                                        id = R.color.pink
                                    )
                                )
                            )
                        }
                    }
                    components.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)) {
                            Text(
                                text = "email", style = TextStyle(
                                    fontSize = 14.sp, color = colorResource(
                                        id = R.color.black
                                    )
                                )
                            )
                            Text(
                                text = it.email, style = TextStyle(
                                    fontSize = 18.sp, color = colorResource(
                                        id = R.color.pink,
                                    )
                                )

                            )
                        }
                    }
                    components.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 15.dp,
                                bottom = 15.dp
                            )
                        ) {
                            Text(
                                text = "Address", style = TextStyle(
                                    fontSize = 14.sp, color = colorResource(
                                        id = R.color.black
                                    )
                                )
                            )
                            Text(
                                text = it.address, style = TextStyle(
                                    fontSize = 14.sp, color = colorResource(
                                        id = R.color.steel
                                    )
                                )
                            )
                        }
                    }
                    components.Card(
                        modifier = Modifier
                            .height(52.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 15.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "user", result
                                    )
                                    navController.navigate(Screen.CAViewProfileScreen.route)
                                },
                            text = "View Social Profile", style = TextStyle(
                                fontSize = 18.sp, color = colorResource(
                                    id = R.color.pink
                                )
                            )
                        )
                    }
                    if (viewModel.detailsScreenUiState.user?.email == CAApplication.sharedPreferences.getUserId()
                    ) {
                        components.Card(
                            modifier = Modifier
                                .height(52.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 15.dp)
                                    .clickable {
                                        navController.navigate(Screen.CAPasswordChangeScreen.route)
                                    },
                                text = "Reset Password", style = TextStyle(
                                    fontSize = 18.sp, color = colorResource(
                                        id = R.color.pink
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
