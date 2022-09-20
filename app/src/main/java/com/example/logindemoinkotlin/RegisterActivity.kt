package com.example.logindemoinkotlin;


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat
import com.example.logindemoinkotlin.AppDatabase
import com.example.logindemoinkotlin.LoginActivity
import com.example.logindemoinkotlin.R
import com.example.logindemoinkotlin.databinding.ActivityRegisterBinding
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass
import com.example.logindemoinkotlin.getPathFromUri
import com.example.logindemoinkotlin.util.Companion.showHide
import com.example.logindemoinkotlin.util.Companion.showToast


class RegisterActivity : AppCompatActivity() {

    var gender = arrayOf("Select Any One", "Male", "Female", "Other")
    private lateinit var binding: ActivityRegisterBinding
    private var imageUri: String? = null
    var id: Int? = null
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
            binding.addImgInRegister.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_addimagelogo))
            imageUri = null
            showHide(binding.minus)
        }
        val adapter = ArrayAdapter(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            gender)
        binding.gender.adapter = adapter
        binding.addImgInRegister.setOnClickListener() {
            /*val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 10)*/
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryActivityResultLauncher.launch(intent)
        }
        val db = AppDatabase.getDatabase(this)
        val intent = intent
        isUpdate = intent.getBooleanExtra("UPDATE", false)
        if (isUpdate) {
            binding.toolbarText.text = "Update"
            getDataFromIntentAndSetInEditText()
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
                showToast(applicationContext, "Please Select Image.")
            } else if (gender.equals("Select Any One")) {
                showToast(applicationContext, "Please Select gender.")
            } else {
                if (isUpdate) {
                    db.userDao().updateRecord(allUserData())
                    finish()
                    showToast(applicationContext,
                        "Update Successfully")
                } else {
                    db.userDao().insertUser(allUserData())
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()

                }
            }
        }
    }

    private fun allUserData(): UserInformationDataClass {
        val uid: Int?
        if (isUpdate) uid = id else uid = null
        return UserInformationDataClass(
            uid,
            binding.FirstNameRegister.text.toString(),
            binding.lastNameRegister.text.toString(),
            binding.registerUsername.text.toString(),
            binding.RegisterPhoneNumber.text.toString(),
            binding.RegisterEmail.text.toString(),
            binding.RegisterPassword.text.toString(),
            binding.RegisterConformPassword.text.toString(),
            binding.gender.selectedItem.toString(),
            imageUri.toString())
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
 */
    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private val galleryActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //here we will handle the result of our intent
        if (result.resultCode == RESULT_OK) {
            /*image picked
            get uri of image
            val imageUri: Uri? = data!!.data
            profileIv.setImageURI(imageUri)
*/
            val data = result.data
            imageUri = getPathFromUri(this, data!!.data!!)
            binding.addImgInRegister.setImageURI(data.data)
            showHide(binding.minus)

        } else {
            //cancelled
            showToast(applicationContext, "Cancelled")
        }
    }

    fun getDataFromIntentAndSetInEditText() {
        val modal: UserInformationDataClass =
            intent.getSerializableExtra("MODAl") as UserInformationDataClass
        id = modal.id
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
            showHide(binding.minus)
        }
        showHide(binding.RegisterToLoginBtn)
    }
}