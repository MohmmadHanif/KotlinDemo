package com.example.logindemoinkotlin

import com.example.logindemoinkotlin.dataclass.apiDataClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/posts")
     fun getAllApiData() : Call<ArrayList<apiDataClass>?>?
}