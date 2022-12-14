package com.example.logindemoinkotlin

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass

@Dao
interface UserDao {
    /*
        @Query("SELECT * FROM user")
        fun getAll(): List

        @Query("SELECT * FROM user WHERE id IN (:userIds)")
        fun loadAllByIds(userIds: IntArray): List
    */
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<UserInformationDataClass>>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun login(email: String, password: String): LiveData<UserInformationDataClass>

    @Query("SELECT * FROM user WHERE email = :email")
    fun checkEmail(email: String): LiveData<UserInformationDataClass>

    @Insert
    fun insertAll(users: UserInformationDataClass)


    @Insert
    fun insertUser(users: UserInformationDataClass): Long
    /* @Query("Update user set first_name = :name, last_name = :lastName , user_name = :userName , phone_number = :phoneNumber , email = :email, password = :password, conform_password = :conPassword, gender = :gender, image = :image where user_name = :usernamee")
     fun updateRecord(
         id: Int?,
         name: String?,
         lastName: String?,
         userName: String?,
         phoneNumber: String?,
         email: String?,
         password: String,
         conPassword :String,
         gender: String?,
         usernamee :String?,
         image :String
     )*/
    @Update
    fun updateRecord(users: UserInformationDataClass)

    @Query("DELETE FROM user WHERE user_name = :username")
    fun deleteByUserName(username: String)

    @Query("Select * From user where email = :email")
    fun getsingle(email: String): LiveData<UserInformationDataClass>
}