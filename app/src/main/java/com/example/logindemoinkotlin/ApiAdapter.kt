package com.example.logindemoinkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemoinkotlin.databinding.ItemLayoutOfApiBinding
import com.example.logindemoinkotlin.dataclass.apiDataClass
import retrofit2.Response

class ApiAdapter(context: Context, private val apiDataList: ArrayList<apiDataClass>) :
    RecyclerView.Adapter<ApiAdapter.viewHolder>() {
    class viewHolder(val binding: ItemLayoutOfApiBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = ItemLayoutOfApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiAdapter.viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val modal:apiDataClass = apiDataList[position]
        holder.binding.apiId.text = modal.id.toString()
        holder.binding.apiTitle.text = modal.title
        holder.binding.apiBody.text = modal.body
    }

    override fun getItemCount(): Int {
        return apiDataList.size
    }


}