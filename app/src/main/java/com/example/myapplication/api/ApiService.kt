package com.example.myapplication.api

import com.example.myapplication.model.Car
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/cars")
    fun getCars(): Call<List<Car>>

    @POST("/api/cars")
    fun addCar(@Body car: Car): Call<Car>
}