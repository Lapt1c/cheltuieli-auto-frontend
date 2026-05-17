package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Expense
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val masina = intent.getSerializableExtra("CHEIE_MASINA") as? com.example.myapplication.model.Car

        if (masina != null) {
            findViewById<TextView>(R.id.tvDetailBrand).text = masina.brand
            findViewById<TextView>(R.id.tvDetailModel).text = masina.model
            findViewById<TextView>(R.id.tvDetailPlate).text = masina.plateNumber

            val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)

            btnAddExpense.setOnClickListener {
                val intent = android.content.Intent(this, AddExpenseActivity::class.java)
                intent.putExtra("CAR_ID", masina.id)
                startActivity(intent)
            }

            val btnDelete = findViewById<Button>(R.id.btnDelete)

            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmare stergere")
                builder.setMessage("Esti sigur ca vrei sa stergi aceasta masina?")

                builder.setPositiveButton("Sterge") { dialog, which ->

                    val retrofit = Retrofit.Builder()
                        .baseUrl(com.example.myapplication.Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(com.example.myapplication.api.ApiService::class.java)

                    apiService.deleteCar(masina.id!!).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@CarDetailActivity, "Masina a fost stearsa!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@CarDetailActivity, "Eroare server: Cod ${response.code()}", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@CarDetailActivity, "Eroare de conexiune la internet", Toast.LENGTH_LONG).show()
                        }
                    })
                }

                builder.setNegativeButton("Anuleaza") { dialog, which ->
                    dialog.dismiss()
                }

                builder.create().show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val masina = intent.getSerializableExtra("CHEIE_MASINA") as? com.example.myapplication.model.Car

        if (masina != null) {
            val tvExpenses = findViewById<TextView>(R.id.tvExpensesList)

            val retrofit = Retrofit.Builder()
                .baseUrl(com.example.myapplication.Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(com.example.myapplication.api.ApiService::class.java)

            apiService.getExpenses(masina.id!!).enqueue(object : Callback<List<Expense>> {
                override fun onResponse(call: Call<List<Expense>>, response: Response<List<Expense>>) {
                    if (response.isSuccessful) {
                        val expenses = response.body()
                        if (expenses.isNullOrEmpty()) {
                            tvExpenses.text = "Nicio cheltuiala adaugata inca."
                        } else {
                            // Construim textul pe care il vom afisa
                            var istoric = "Istoric Cheltuieli:\n\n"
                            for (exp in expenses) {
                                istoric += "• ${exp.date} | ${exp.type} | ${exp.amount} RON\n  Desc: ${exp.description}\n\n"
                            }
                            tvExpenses.text = istoric
                        }
                    }
                }

                override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                    tvExpenses.text = "Eroare la incarcarea cheltuielilor."
                }
            })
        }
    }
}