package com.example.logindemoinkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class util(context: Context) {
    companion object{


    var sharedPreferences: SharedPreferences? = null
    var editor: Editor? = null

  /*  init {
        sharedPreferences = context.getSharedPreferences("kotlinsharedpreference", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit();
    }*/

    fun showToast(context: Context, massage: String) {
        Toast.makeText(context, massage, Toast.LENGTH_LONG).show()
    }

    fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun sharephef(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("kotlinsharedpreference",
                Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
    }

    fun sharPrefgetBoolean(key: String, boolean: Boolean) {
        val loginCheck: Boolean = sharedPreferences?.getBoolean(key, boolean)!!
    }
}
}



