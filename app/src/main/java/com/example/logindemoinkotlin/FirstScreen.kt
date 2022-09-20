package com.example.logindemoinkotlin
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.logindemoinkotlin.databinding.ActivitySplashScreenBinding
import com.example.logindemoinkotlin.util.Companion.showToast


class FirstScreen : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var  binding :ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            }else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }

        /*installSplashScreen().apply {
            setKeepOnScreenCondition{
                Handler(Looper.getMainLooper()).postDelayed({
                }, 3000)
            }
        }*/
        Handler(Looper.getMainLooper()).postDelayed({
            val loginCheck:Boolean = sharedPreferences.getBoolean("login",false)
            if (loginCheck){
                startActivity(Intent(applicationContext,HomeActivity::class.java))
                showToast(applicationContext,"You already have Login")
            }else{
                startActivity(Intent(applicationContext,LoginActivity::class.java))
            }
            finish()
        }, 1000)
    }
}