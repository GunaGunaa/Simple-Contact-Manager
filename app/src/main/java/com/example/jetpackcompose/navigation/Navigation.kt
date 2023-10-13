package com.example.jetpackcompose.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.model.UserData
import com.example.jetpackcompose.screen.*

class Navigation {
    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun NavController() {
        val loginScreen = LoginScreen()
        val signupScreen = SignupScreen()
        val contactListScreen = ContactListScreen()
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
            composable(route = Screen.SplashScreen.route) {
                SplashScreen(navController = navController)
            }
            composable(Screen.LoginScreen.route) {
                loginScreen.SetLogin(navController)
            }
            composable(Screen.SignupScreen.route) {
                signupScreen.SetSignup(navController)
            }
            composable(Screen.ContactListScreen.route) {
                contactListScreen.SetContactList(navController)
            }
            composable(Screen.DetailsScreen.route) {
                val result =
                    navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
                CADetailsScreen().
                SetDetailsScreen(navController, result)
            }
            composable(Screen.EditScreen.route) {
                val result =
                    navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
                CAEditScreen().SetEditScreen(navController, user = result)
            }
            composable(Screen.CAPasswordChangeScreen.route) {
                CAPasswordChangeScreen().SetChangePassword(navController)
            }
            composable(Screen.CAViewProfileScreen.route) {
                val result =
                    navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
                CAViewSocialProfile().SetSocialScreen(navController,result)
            }
        }
    }
}