package com.example.logindemoinkotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.logindemoinkotlin.databinding.CardViewDesignBinding
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass


class ShowUserListAdapter( val mList: ArrayList<UserInformationDataClass>,val context: Context) :
    RecyclerView.Adapter<ShowUserListAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = CardViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val modal: UserInformationDataClass = mList[position]
        //Uri myUri = Uri.parse("http://stackoverflow.com")
        holder.binding.username.text = modal.userName
        holder.binding.firstname.text = modal.firstName
        holder.binding.lastname.text = modal.lastName
        holder.binding.email.text = modal.email
        holder.binding.phonenumber.text = modal.phoneNumber
        holder.binding.showgender.text = modal.gender
        if (modal.image.isNotEmpty()){
            val bmp: Bitmap = BitmapFactory.decodeFile(modal.image)
            holder.binding.showImg.setImageBitmap(bmp)
        }
       // holder.binding.showImg.setImageURI(Uri.parse(modal.image))
       // Glide.with(context).load(modal.image).into(holder.binding.showImg)
        //Glide.with(holder.itemView.context).load(currentItem.img).into(holder.itemView.imageView2)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun deleteUser(position : Int) {
        val username:String = mList[position].userName!!
        val db = AppDatabase.getDatabase(context)
        db.userDao().deleteByUserName(username)
        mList.removeAt(position)
        notifyItemRemoved(position)
    }
    class viewHolder(val binding: CardViewDesignBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
