package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val button= findViewById<ImageButton>(R.id.perfil)

        button.setOnClickListener{
            val intent = Intent(this,Editar_Perfil::class.java)
            startActivity(intent)
        }
        }
    }
