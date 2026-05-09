package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarViewModel(private val apiService: ApiService) : ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> get() = _cars

    fun fetchCars() {
        apiService.getCars().enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    _cars.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
            }
        })
    }
}