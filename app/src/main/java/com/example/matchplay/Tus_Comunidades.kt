package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Tus_Comunidades : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tus_comunidades)

        val button = findViewById<ImageButton>(R.id.iniciar255)

        button.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        val button1 = findViewById<ImageButton>(R.id.imageButton2)

        button1.setOnClickListener {
            val intent = Intent(this, Crear_Comunidad::class.java)
            startActivity(intent)

        }
    }}