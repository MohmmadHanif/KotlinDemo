package com.example.logindemoinkotlin


import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.logindemoinkotlin.databinding.ActivityRegisterBinding
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    var gender = arrayOf("Select Any One", "Male", "Female", "Other")
    private lateinit var binding: ActivityRegisterBinding
    private var imageUri: String? = null
    private var bitmap: Bitmap? = null

    private var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBackImg.setOnClickListener() {
            finish()
        }
        binding.minus.setOnClickListener() {
/*
            binding.addImgInRegister.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.));*/
        }
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, gender
        )
        binding.gender.adapter = adapter
        binding.addImgInRegister.setOnClickListener() {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 10)
        }
        val db = AppDatabase.getDatabase(this)
        val intent = intent
        isUpdate = intent.getBooleanExtra("UPDATE", false)
        if (isUpdate) {
            binding.toolbarText.text = "Update"
            val modal: UserInformationDataClass =
                intent.getSerializableExtra("MODAl") as UserInformationDataClass
            val uFName = modal.firstName
            val uLName = modal.lastName
            val uUsername = modal.userName
            val uPhoneNNumber = modal.phoneNumber
            val uPassword = modal.password
            val uCPassword = modal.conformPassword
            val uEmail = modal.email
            val uGender = modal.gender
            imageUri = modal.image

            fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
            binding.FirstNameRegister.text = uFName!!.toEditable()
            binding.lastNameRegister.text = uLName!!.toEditable()
            binding.registerUsername.text = uUsername!!.toEditable()
            binding.RegisterPhoneNumber.text = uPhoneNNumber!!.toEditable()
            binding.RegisterPassword.text = uPassword!!.toEditable()
            binding.RegisterConformPassword.text = uCPassword!!.toEditable()
            binding.RegisterEmail.text = uEmail!!.toEditable()
            binding.gender.setSelection(getIndex(binding.gender, uGender!!))
            if (modal.image.isNotEmpty()) {
                val bmp: Bitmap = BitmapFactory.decodeFile(modal.image)
                binding.addImgInRegister.setImageBitmap(bmp)
                binding.minus.visibility = View.VISIBLE
            }
            binding.RegisterToLoginBtn.visibility = View.GONE
        }
        binding.RegisterBtn.setOnClickListener() {

            val firstName = binding.FirstNameRegister.text.toString()
            val lastName = binding.lastNameRegister.text.toString()
            val username = binding.registerUsername.text.toString()
            val phoneNumber = binding.RegisterPhoneNumber.text.toString()
            val email = binding.RegisterEmail.text.toString()
            val password = binding.RegisterPassword.text.toString()
            val conPassword = binding.RegisterConformPassword.text.toString()
            val gender = binding.gender.selectedItem.toString()
            val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$".toRegex()
            if (firstName.isEmpty()) {
                binding.FirstNameRegister.error = "First name can't be empty"
                binding.FirstNameRegister.requestFocus()
            } else if (lastName.isEmpty()) {
                binding.lastNameRegister.error = "Lastname name can't be empty"
                binding.lastNameRegister.requestFocus()
            } else if (username.isEmpty()) {
                binding.registerUsername.error = "Username name can't be empty"
                binding.registerUsername.requestFocus()
            } else if (phoneNumber.isEmpty()) {
                binding.RegisterPhoneNumber.error = "PhoneNumber name can't be empty"
                binding.RegisterPhoneNumber.requestFocus()
            } else if (email.isEmpty()) {
                binding.RegisterEmail.error = "Email name can't be empty"
                binding.RegisterEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.RegisterEmail.error = "Enter Valid Email"
                binding.RegisterEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.RegisterPassword.error = "Password name can't be empty"
                binding.RegisterPassword.requestFocus()
            } else if (!pattern.matches(password)) {
                binding.RegisterPassword.error = "Password to easy"
                binding.RegisterPassword.requestFocus()
            } else if (conPassword.isEmpty()) {
                binding.RegisterConformPassword.error = "Conform Password can't be empty"
                binding.RegisterConformPassword.requestFocus()
            } else if (password != conPassword) {
                binding.RegisterPassword.error = "Password Not Match"
                binding.RegisterPassword.requestFocus()
            } else if (phoneNumber.length <= 9 || phoneNumber.length >= 11) {
                binding.RegisterPassword.error = "number must be 10 digits"
                binding.RegisterPassword.requestFocus()
            } else if (imageUri == null) {
                Toast.makeText(applicationContext, "Please Select Image.", Toast.LENGTH_LONG).show()
            } else if (gender.equals("Select Any One")) {
                Toast.makeText(applicationContext, "Please Select gender.", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (isUpdate) {
                    val sfirstName = binding.FirstNameRegister.text.toString()
                    val slastName = binding.lastNameRegister.text.toString()
                    val susername = binding.registerUsername.text.toString()
                    val sphoneNumber = binding.RegisterPhoneNumber.text.toString()
                    val semail = binding.RegisterEmail.text.toString()
                    val spassword = binding.RegisterPassword.text.toString()
                    val sconPassword = binding.RegisterConformPassword.text.toString()
                    val sgender = binding.gender.selectedItem.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        db.userDao().updateRecord(sfirstName,
                            slastName,
                            susername,
                            sphoneNumber,
                            semail,
                            spassword,
                            sconPassword,
                            sgender,
                            susername,
                            imageUri.toString())
                        startActivity(Intent(applicationContext, ShowUserListActivity::class.java))
                        finish()
                    }

                    /*Toast.makeText(
                        applicationContext,
                        "Update Successfully",
                        Toast.LENGTH_LONG
                    ).show()*/
                } else {
                    db.userDao().checkEmail(username).observe(this, Observer {
                        if (it != null && it.userName.equals(username)) {
                            Toast.makeText(
                                applicationContext,
                                "This Username already used.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                db.userDao().insertAll(
                                    UserInformationDataClass(
                                        null,
                                        firstName,
                                        lastName,
                                        username,
                                        phoneNumber,
                                        email,
                                        password,
                                        conPassword,
                                        gender,
                                        imageUri.toString()

                                    )
                                )
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                finish()
                            }
                        }
                    })
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 10) {
            if (data != null) {
                imageUri = getPathFromUri(this, data.data!!)
                binding.addImgInRegister.setImageURI(data.data)
                binding.minus.visibility = View.VISIBLE
            }
            //binding.addImgInRegister.setImageBitmap(bitmap)
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }
}