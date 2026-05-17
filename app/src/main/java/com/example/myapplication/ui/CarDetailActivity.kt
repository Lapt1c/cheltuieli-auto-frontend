package com.example.myapplication.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Expense
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
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
            val pieChart = findViewById<PieChart>(R.id.pieChart)

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
                            pieChart?.visibility = View.GONE // Ascundem graficul
                        } else {
                            pieChart?.visibility = View.VISIBLE // Afisam graficul

                            // 1. Textul simplu
                            var istoric = "Istoric Cheltuieli:\n\n"
                            for (exp in expenses) {
                                istoric += "• ${exp.date} | ${exp.type} | ${exp.amount} RON\n  Desc: ${exp.description}\n\n"
                            }
                            tvExpenses.text = istoric

                            // 2. Graficul PieChart
                            val groupedExpenses = expenses.groupBy { it.type }
                                .mapValues { entry -> entry.value.sumOf { it.amount } }

                            val entries = ArrayList<PieEntry>()
                            var totalGeneral = 0.0

                            for ((type, totalAmount) in groupedExpenses) {
                                entries.add(PieEntry(totalAmount.toFloat(), type.name))
                                totalGeneral += totalAmount
                            }

                            val dataSet = PieDataSet(entries, "")
                            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                            dataSet.valueTextSize = 14f
                            dataSet.valueTextColor = Color.WHITE

                            val data = PieData(dataSet)

                            pieChart?.data = data
                            pieChart?.description?.isEnabled = false
                            pieChart?.centerText = "Total:\n${totalGeneral} RON"
                            pieChart?.setCenterTextSize(16f)
                            pieChart?.animateY(1000)
                            pieChart?.invalidate()
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