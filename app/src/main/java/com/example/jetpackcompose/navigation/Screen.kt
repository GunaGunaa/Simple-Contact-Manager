package com.example.jetpackcompose.navigation


sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("main_screen")
    object SignupScreen : Screen("signup_screen")
    object ContactListScreen : Screen("contact_screen")
    object DetailsScreen : Screen("details_screen")
    object EditScreen : Screen("edit_screen")
    object CAPasswordChangeScreen : Screen("password_change_screen")
    object CAViewProfileScreen:Screen("view_profile_screen")
}
