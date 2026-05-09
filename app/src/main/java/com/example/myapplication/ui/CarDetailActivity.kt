package com.example.myapplication.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Car

class CarDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val masina = intent.getSerializableExtra("CHEIE_MASINA") as? Car

        if (masina != null) {
            findViewById<TextView>(R.id.tvDetailBrand).text = masina.brand
            findViewById<TextView>(R.id.tvDetailModel).text = masina.model
            findViewById<TextView>(R.id.tvDetailPlate).text = masina.plateNumber
        }
    }
}