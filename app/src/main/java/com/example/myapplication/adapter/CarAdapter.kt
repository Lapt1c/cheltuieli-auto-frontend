package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Car

class CarAdapter(
    private val carList: List<Car>,
    private val onItemClick: (Car) -> Unit // Funcția de click
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarPlate: TextView = itemView.findViewById(R.id.tvCarPlate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.tvCarName.text = "${car.brand} ${car.model}"
        holder.tvCarPlate.text = car.plateNumber

        holder.itemView.setOnClickListener { onItemClick(car) }
    }

    override fun getItemCount(): Int = carList.size
}