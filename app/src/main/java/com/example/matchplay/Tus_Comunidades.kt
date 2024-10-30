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
        val buttone = findViewById<ImageButton>(R.id.imageButton2)

        buttone.setOnClickListener {
            val intent = Intent(this, Crear_Comunidad::class.java)
            startActivity(intent)
        }
    }}