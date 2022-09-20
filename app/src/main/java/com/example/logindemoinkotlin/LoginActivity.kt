package com.example.logindemoinkotlin


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.example.logindemoinkotlin.databinding.ActivityLoginBinding
import com.example.logindemoinkotlin.util.Companion.showToast
import com.google.gson.Gson


class LoginActivity : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         *SharedPreferences
         */
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        //permission
        permission_fn()

        //Show UserList Button

        binding.showUserListBtn.setOnClickListener() {
            startActivity(Intent(applicationContext, ShowUserListActivity::class.java))
        }
        //Back Button
        binding.loginBackImg.setOnClickListener() {
            finish()
        }
        //Login To Register Button
        binding.loginToRegisterBtn.setOnClickListener() {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        //Room Data Base Reference
        val db = AppDatabase.getDatabase(this)

        //Login Button
        binding.loginBtn.setOnClickListener() {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()
            if (email.isEmpty()) {
                binding.loginEmail.error = "Email can't be Empty"
                binding.loginEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.loginEmail.error = "Password can't be Empty"
                binding.loginEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.loginEmail.error = "Please Enter Valid Email Address"
                binding.loginEmail.requestFocus()
            } else {
                //Login Email Check
                db.userDao().checkEmail(email)
                    .observe(this, Observer { it ->
                        if (it != null && it.email.equals(email)) {
                            db.userDao().login(
                                binding.loginEmail.text.toString().trim(),
                                binding.loginPassword.text.toString().trim()
                            )
                                .observe(this, Observer {
                                    if (it != null && it.password.equals(
                                            password
                                        )
                                    ) {
                                        showToast(applicationContext, "Login Successful")
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                HomeActivity::class.java
                                            )
                                        )
                                        //Store Login User Information in SharedPreferences
                                        db.userDao().getsingle(email).observe(this) {
                                            editor.apply {
                                                putBoolean("login", true)
                                                val gson = Gson()
                                                val json = gson.toJson(it)
                                                putString("modal", json)
                                                apply()
                                            }
                                        }
                                    } else {
                                        showToast(applicationContext, "Wrong Password")
                                    }
                                })
                        } else {
                            showToast(applicationContext, "Wrong Username")
                        }
                    })
            }
        }
    }

    //self Check Permission AlertDialog
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Permission is needed to access files from your device...")
                .setPositiveButton("OK"
                ) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 1
                    )
                }
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    //self Check Permission
    private fun permission_fn() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else {
            requestStoragePermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(applicationContext, "Thanks for enabling the permission")

                //do something permission is allowed here....
            } else {
                Toast.makeText(this, "Please allow the Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //permission
    /* fun checkPermission(permission: String, requestCode: Int) {
         if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

             // Requesting the permission
             ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
         } else {
             Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
         }
     }

     // This function is called when the user accepts or decline the permission.
     // Request Code is used to check which permission called this function.
     // This request code is provided when the user is prompt for permission.
     override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>,
                                             grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if (requestCode == READ_EXTERNAL_STORAGE) {
             if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Toast.makeText(this, "Read Permission Granted", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this, "Read Permission Denied", Toast.LENGTH_SHORT).show()
             }
         } else if (requestCode == STORAGE_PERMISSION_CODE) {
             if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
             }
         }
     }*/
}
