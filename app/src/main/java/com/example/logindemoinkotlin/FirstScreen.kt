package com.example.logindemoinkotlin
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.logindemoinkotlin.databinding.ActivitySplashScreenBinding


class FirstScreen : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var  binding :ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        Handler(Looper.getMainLooper()).postDelayed({
            val loginCheck:Boolean = sharedPreferences.getBoolean("login",false)
            if (loginCheck){
                startActivity(Intent(applicationContext,HomeActivity::class.java))
            }else{
                startActivity(Intent(applicationContext,LoginActivity::class.java))
            }
            finish()
        }, 1000)
    }
}