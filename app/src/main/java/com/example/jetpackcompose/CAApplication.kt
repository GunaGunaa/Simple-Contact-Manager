package com.example.jetpackcompose

import android.app.Application
import android.content.Context
import android.os.Trace
import com.example.jetpackcompose.network.CASharedPreference
import com.google.firebase.perf.FirebasePerformance
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CAApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = CASharedPreference(applicationContext)
        contextApplication = this
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
    }

    companion object {
        lateinit var sharedPreferences: CASharedPreference
            private set

        lateinit var contextApplication: Context
            private set
    }
}