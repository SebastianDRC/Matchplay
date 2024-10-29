package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tus_Comunidades : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tus_comunidades)

        val button = findViewById<ImageButton>(R.id.iniciarlogin25)

        button.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        // Obtener los datos pasados desde MainActivity
        val name = intent.getStringExtra("EXTRA_NAME")
        val sport = intent.getStringExtra("EXTRA_SPORT")
        val description = intent.getStringExtra("EXTRA_DESCRIPTION")

        // Log para verificar los datos recibidos
        Log.d("CommunityDetailActivity", "Datos recibidos: Nombre=$name, Deporte=$sport, Descripción=$description")

        // Mostrar los datos en los TextViews
        findViewById<TextView>(R.id.nameTextView).text = "Nombre: $name"
        findViewById<TextView>(R.id.sportTextView).text = "Deporte: $sport"
        findViewById<TextView>(R.id.descriptionTextView).text = "Descripción: $description"
    }
}