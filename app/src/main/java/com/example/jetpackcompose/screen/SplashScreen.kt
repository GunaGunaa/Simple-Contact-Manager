package com.example.jetpackcompose.screen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.navigation.Navigation
import com.example.jetpackcompose.navigation.Screen
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener
import com.google.firebase.inappmessaging.model.Action
import com.google.firebase.inappmessaging.model.InAppMessage
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val navigation = Navigation()
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                navigation.NavController()
            }
        }
        //To integrate firebase push notification
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
        FirebaseInAppMessaging.getInstance().triggerEvent("in_app")
        val listener =
            FirebaseInAppMessagingClickListener { _, _ ->
                Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show()
            }

        FirebaseInAppMessaging.getInstance().addClickListener(listener)
        Firebase.messaging.isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
        })
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavHostController) {
    val system = rememberSystemUiController()
    val coroutineScope = rememberCoroutineScope()
    system.setSystemBarsColor(color = MaterialTheme.colors.background)
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.background
            )
    ) {
        val scale = remember {
            androidx.compose.animation.core.Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 1.5f, animationSpec = tween(durationMillis = 800, easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
            )
            coroutineScope.launch {
                delay(3000)
                navController.navigate(Screen.LoginScreen.route) {
                    navController.popBackStack()
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "contact_logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTheme {
//        SetLogin()
//        ContactSplashScreen()
    }
}
