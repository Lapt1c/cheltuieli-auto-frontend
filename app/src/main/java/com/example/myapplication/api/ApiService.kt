package com.example.myapplication.api

import com.example.myapplication.model.Car
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/api/cars")
    fun getCars(): Call<List<Car>>

    @POST("/api/cars")
    fun addCar(@Body car: Car): Call<Car>

    @DELETE("/api/cars/{id}")
    fun deleteCar(@Path("id") id: Long): Call<Void>
}