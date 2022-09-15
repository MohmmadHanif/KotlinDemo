package com.example.logindemoinkotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.logindemoinkotlin.dataclass.UserInformationDataClass
import java.math.MathContext

@Database(entities = arrayOf(UserInformationDataClass::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        /**
         *         If In this INSTANCE of anything write the update all thread Or single to thread value is update
         */
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null){
                synchronized(this){  //use for restrict to create multiple instance
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "UserInformation.DB").allowMainThreadQueries().build()
                }
            }
            return INSTANCE!!
        }

    }
}