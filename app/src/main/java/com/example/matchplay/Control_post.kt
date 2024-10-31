package com.example.matchplay

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ControlPost(
    private val activity: Activity,
    private val dataset: List<Post>
) : RecyclerView.Adapter<ControlPost.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val nombreComu: TextView = view.findViewById(R.id.textView122)
        //val imagen1: ImageButton = view.findViewById(R.id.imageButton4)
        val date: TextView = view.findViewById(R.id.editTextDate)
        val username: TextView = view.findViewById(R.id.textView)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.activity_menu, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = dataset[position]

        // Asigna el nombre de la comunidad y la imagen al ViewHolder
        holder.nombreComu.text = post.comuniName
       // holder.imagen1.setImageDrawable(post.imagen)

        // Formatear y asignar la fecha
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.date.text = sdf.format(post.date)

        // Asegúrate de usar estos elementos según lo necesites
        holder.username.text = post.username // Ejemplo de asignación

    }
}
