package com.example.matchplay

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Crear_Comunidad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_comunidad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack = findViewById<ImageButton>(R.id.iniciarlogin)
        btnBack.setOnClickListener {
            val intent = Intent(this, Tus_Comunidades::class.java)
            startActivity(intent)
        }
        // Botón Crear Comunidad
        val btnCreate = findViewById<Button>(R.id.button)
        btnCreate.setOnClickListener {
            val name = findViewById<EditText>(R.id.imageView12).text.toString()
            val deporte = findViewById<EditText>(R.id.imageView13).text.toString()
            val description = findViewById<EditText>(R.id.imageView14).text.toString()

            if (name.isNotEmpty() && deporte.isNotEmpty() && description.isNotEmpty()) {
                Toast.makeText(this, "Comunidad creada con éxito", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
