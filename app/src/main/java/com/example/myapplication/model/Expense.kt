package com.example.myapplication.model

data class Expense(
    val type: ExpenseType,
    val amount: Double,
    val date: String,
    val description: String
)