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
    fun deleteCar(carId: Long, onResult: (Boolean) -> Unit) {
        apiService.deleteCar(carId).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    onResult(true)

                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}