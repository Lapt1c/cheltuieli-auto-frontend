package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Constants
import com.example.myapplication.R
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car)

        // Preluam masina veche trimisa din ecranul de detalii
        val masina = intent.getSerializableExtra("CHEIE_MASINA") as? Car ?: return

        val etBrand = findViewById<EditText>(R.id.etEditBrand)
        val etModel = findViewById<EditText>(R.id.etEditModel)
        val etPlate = findViewById<EditText>(R.id.etEditPlate)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateCar)

        // Completam automat campurile cu datele actuale ca sa le poti modifica
        etBrand.setText(masina.brand)
        etModel.setText(masina.model)
        etPlate.setText(masina.plateNumber)

        btnUpdate.setOnClickListener {
            val updatedBrand = etBrand.text.toString().trim()
            val updatedModel = etModel.text.toString().trim()
            val updatedPlate = etPlate.text.toString().trim()

            if (updatedBrand.isEmpty() || updatedModel.isEmpty() || updatedPlate.isEmpty()) {
                Toast.makeText(this, "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cream noul obiect cu datele modificate
            val masinaActualizata = Car(
                id = masina.id,
                brand = updatedBrand,
                model = updatedModel,
                plateNumber = updatedPlate
            )

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            // Trimitem noile date prin PUT
            apiService.updateCar(masina.id!!, masinaActualizata).enqueue(object : Callback<Car> {
                override fun onResponse(call: Call<Car>, response: Response<Car>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditCarActivity, "Masina actualizata cu succes!", Toast.LENGTH_SHORT).show()
                        finish() // Inchidem ecranul de editare
                    } else {
                        Toast.makeText(this@EditCarActivity, "Eroare: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Car>, t: Throwable) {
                    Toast.makeText(this@EditCarActivity, "Eroare de conexiune!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}