package com.example.logindemoinkotlin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

open class MyAppClass : Application() {
    val Context.myApp: MyAppClass get() = applicationContext as MyAppClass
    var context:Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val sharedPrefFile = "kotlinsharedpreference"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val prefs = applicationContext.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)

    }
}