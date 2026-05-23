package com.example.myapplication.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Car
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CarAdapter(
    private val carList: List<Car>,
    private val onItemClick: (Car) -> Unit // Funcția de click
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarPlate: TextView = itemView.findViewById(R.id.tvCarPlate)
        // Am adaugat cele doua campuri noi pentru alerte
        val tvAlertItp: TextView = itemView.findViewById(R.id.tvAlertItp)
        val tvAlertRca: TextView = itemView.findViewById(R.id.tvAlertRca)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.tvCarName.text = "${car.brand} ${car.model}"
        holder.tvCarPlate.text = car.plateNumber

        // Resetăm textul la fiecare randare pentru a evita suprapunerile de date cand faci scroll
        holder.tvAlertItp.text = ""
        holder.tvAlertRca.text = ""

        val azi = LocalDate.now()

        // LOGICA PENTRU ITP
        if (!car.itpExpiration.isNullOrEmpty()) {
            try {
                val dataItp = LocalDate.parse(car.itpExpiration)
                val zilePanaLaItp = ChronoUnit.DAYS.between(azi, dataItp)

                if (zilePanaLaItp < 0) {
                    holder.tvAlertItp.text = "❌ ITP Expirat de ${-zilePanaLaItp} zile!"
                    holder.tvAlertItp.setTextColor(Color.RED)
                } else if (zilePanaLaItp <= 30) {
                    holder.tvAlertItp.text = "⚠️ ITP expiră în $zilePanaLaItp zile!"
                    holder.tvAlertItp.setTextColor(Color.parseColor("#FFA500")) // Portocaliu
                } else {
                    holder.tvAlertItp.text = "✅ ITP Valabil"
                    holder.tvAlertItp.setTextColor(Color.parseColor("#006400")) // Verde inchis
                }
            } catch (e: Exception) {
                holder.tvAlertItp.text = "Data ITP invalidă"
            }
        }

        // LOGICA PENTRU RCA
        if (!car.rcaExpiration.isNullOrEmpty()) {
            try {
                val dataRca = LocalDate.parse(car.rcaExpiration)
                val zilePanaLaRca = ChronoUnit.DAYS.between(azi, dataRca)

                if (zilePanaLaRca < 0) {
                    holder.tvAlertRca.text = "❌ RCA Expirat de ${-zilePanaLaRca} zile!"
                    holder.tvAlertRca.setTextColor(Color.RED)
                } else if (zilePanaLaRca <= 30) {
                    holder.tvAlertRca.text = "⚠️ RCA expiră în $zilePanaLaRca zile!"
                    holder.tvAlertRca.setTextColor(Color.parseColor("#FFA500")) // Portocaliu
                } else {
                    holder.tvAlertRca.text = "✅ RCA Valabil"
                    holder.tvAlertRca.setTextColor(Color.parseColor("#006400")) // Verde inchis
                }
            } catch (e: Exception) {
                holder.tvAlertRca.text = "Data RCA invalidă"
            }
        }

        holder.itemView.setOnClickListener { onItemClick(car) }
    }

    override fun getItemCount(): Int = carList.size
}