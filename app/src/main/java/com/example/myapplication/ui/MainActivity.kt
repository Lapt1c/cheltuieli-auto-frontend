package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CarAdapter
import com.example.myapplication.api.ApiService
import com.example.myapplication.viewmodel.CarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var api: ApiService
    private lateinit var viewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.1.132:8080")
            .baseUrl("http://172.20.10.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)

        viewModel = CarViewModel(api)

        viewModel.cars.observe(this) { lista ->
            recyclerView.adapter = CarAdapter(lista) { masinaSelectata ->
                val intent = Intent(this, CarDetailActivity::class.java)
                intent.putExtra("CHEIE_MASINA", masinaSelectata)
                startActivity(intent)
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAddCar).setOnClickListener {
            startActivity(Intent(this, AddCarActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCars()
    }
}