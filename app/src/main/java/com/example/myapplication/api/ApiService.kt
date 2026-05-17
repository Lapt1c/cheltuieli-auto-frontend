package com.example.myapplication.api

import com.example.myapplication.model.Car
import com.example.myapplication.model.Expense
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

    @POST("/api/cars/{carId}/expenses")
    fun addExpense(@Path("carId") carId: Long, @Body expense: Expense): Call<Void>

    @GET("/api/cars/{carId}/expenses")
    fun getExpenses(@Path("carId") carId: Long): Call<List<Expense>>
}