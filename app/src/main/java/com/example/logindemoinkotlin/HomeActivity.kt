package com.example.logindemoinkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logindemoinkotlin.databinding.ActivityHomeBinding
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass
import com.example.logindemoinkotlin.dataclass.apiDataClass
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var apiList: ArrayList<apiDataClass>
    private val sharedPrefFile = "kotlinsharedpreference"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
         val sharedPrefFile = "kotlinsharedpreference"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        apiList = ArrayList()

        val gson = Gson()
        val json: String = sharedPreferences.getString("modal", "").toString()
        val obj = gson.fromJson(json,UserInformationDataClass::class.java )
        binding.firstname.text = obj.firstName
        binding.lastname.text = obj.lastName
        binding.username.text = obj.userName
        binding.phonenumber.text = obj.phoneNumber
        binding.showgender.text = obj.gender
        binding.email.text = obj.email

        if (obj.image.isNotEmpty()){
            val bmp: Bitmap = BitmapFactory.decodeFile(obj.image)
            binding.showImg.setImageBitmap(bmp)
        }
        // h

        binding.logout.setOnClickListener() {
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }
        GlobalScope.launch {
            getNews()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    private fun getNews() {
        binding.ProgressBar.visibility = View.VISIBLE
        val userApi = ApiUtils.getInstance().create(ApiInterface::class.java);
        val call: Call<ArrayList<apiDataClass>?>? = userApi.getAllApiData()


        call!!.enqueue(object : Callback<ArrayList<apiDataClass>?> {
            override fun onResponse(
                call: Call<ArrayList<apiDataClass>?>,
                response: Response<ArrayList<apiDataClass>?>,
            ) {
                if (response.isSuccessful) {
                    apiList = response.body()!!
                }

                val adapter = ApiAdapter(applicationContext, apiList)
                binding.apiRv.adapter = adapter
                binding.ProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ArrayList<apiDataClass>?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
                binding.ProgressBar.visibility = View.GONE
            }


        })
    }
}
