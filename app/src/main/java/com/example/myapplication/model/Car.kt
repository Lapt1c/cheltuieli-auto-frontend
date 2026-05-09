package com.example.myapplication.model

import java.io.Serializable

data class Car(
    val id: Long? = null,
    val brand: String,
    val model: String,
    val plateNumber: String
) : Serializable