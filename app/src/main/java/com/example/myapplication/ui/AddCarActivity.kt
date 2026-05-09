package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        val etBrand = findViewById<EditText>(R.id.etBrand)
        val etModel = findViewById<EditText>(R.id.etModel)
        val etPlate = findViewById<EditText>(R.id.etPlate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.20.10.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        btnSave.setOnClickListener {
            val brand = etBrand.text.toString()
            val model = etModel.text.toString()
            val plate = etPlate.text.toString()

            if (brand.isEmpty() || model.isEmpty() || plate.isEmpty()) {
                Toast.makeText(this, "Te rog completează toate datele!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newCar = Car(brand = brand, model = model, plateNumber = plate)
            api.addCar(newCar).enqueue(object : Callback<Car> {
                override fun onResponse(call: Call<Car>, response: Response<Car>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddCarActivity, "Masina salvata cu succes!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddCarActivity, "Eroare server: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Car>, t: Throwable) {
                    Toast.makeText(this@AddCarActivity, "Eroare rețea: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}