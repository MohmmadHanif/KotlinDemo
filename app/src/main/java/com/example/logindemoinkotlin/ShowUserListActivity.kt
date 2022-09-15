package com.example.logindemoinkotlin

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemoinkotlin.databinding.ActivityShowUserListBinding
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass
import com.example.logindemoinkotlin.dataclass.swipeGester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowUserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowUserListBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db: AppDatabase = AppDatabase.getDatabase(this)
        binding.showUserBackImg.setOnClickListener(){
            finish()
        }

       // binding.recyclerview.layoutManager = LinearLayoutManager(this)
        db.userDao().getAll().observe(this) {
            val adapter = ShowUserListAdapter(it as ArrayList<UserInformationDataClass>, this)
            binding.recyclerview.adapter = adapter

            val swipeGester = object : swipeGester() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val position = viewHolder.adapterPosition
                            val username:String = it[position].userName!!
                            db.userDao().deleteByUserName(username)
                            it.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(applicationContext, "Delete $username", Toast.LENGTH_LONG).show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            var intent = Intent(applicationContext,RegisterActivity::class.java)
                            intent.putExtra("UPDATE",true)
                            intent.putExtra("MODAl",it[viewHolder.adapterPosition])
                            startActivity(intent)
                            Toast.makeText(applicationContext, "Update. ${it[viewHolder.adapterPosition].userName}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            val touchHelper = ItemTouchHelper(swipeGester)
            touchHelper.attachToRecyclerView(binding.recyclerview)

        }

        }

}