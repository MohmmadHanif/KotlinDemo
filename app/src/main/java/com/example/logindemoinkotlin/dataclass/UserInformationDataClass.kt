package com.example.logindemoinkotlin.dataclass

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "user")
data class UserInformationDataClass (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "user_name") var userName: String? = "",
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "conform_password") val conformPassword: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "image") var image: String
):Serializable
