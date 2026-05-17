package com.example.myapplication.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Expense
import com.example.myapplication.model.ExpenseType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val carId = intent.getLongExtra("CAR_ID", -1)

        val spinnerType = findViewById<Spinner>(R.id.spinnerExpenseType)
        val etAmount = findViewById<EditText>(R.id.etExpenseAmount)
        val etDate = findViewById<EditText>(R.id.etExpenseDate)
        val etDesc = findViewById<EditText>(R.id.etExpenseDesc)
        val btnSave = findViewById<Button>(R.id.btnSaveExpense)

        val types = ExpenseType.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerType.adapter = adapter

        btnSave.setOnClickListener {
            val typeStr = spinnerType.selectedItem.toString()
            val amountStr = etAmount.text.toString()
            val dateStr = etDate.text.toString()
            val descStr = etDesc.text.toString()

            if (amountStr.isEmpty() || dateStr.isEmpty()) {
                Toast.makeText(this, "Completeaza suma si data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expense(
                type = ExpenseType.valueOf(typeStr),
                amount = amountStr.toDouble(),
                date = dateStr,
                description = descStr
            )

            // Trimitem datele la server
            val retrofit = Retrofit.Builder()
                .baseUrl(com.example.myapplication.Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            apiService.addExpense(carId, expense).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddExpenseActivity, "Cheltuiala salvata!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddExpenseActivity, "Eroare: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AddExpenseActivity, "Eroare retea!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}