package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val buttonperfil = findViewById<ImageButton>(R.id.perfilbotton)
        buttonperfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
        val buttoncrearcomunidad = findViewById<ImageButton>(R.id.imageButton7)
        buttoncrearcomunidad.setOnClickListener {
            val intent = Intent(this, Crear_Comunidad::class.java)
            startActivity(intent)
        }
        val buttoncomunidades = findViewById<ImageButton>(R.id.crearcomunidades)
        buttoncomunidades.setOnClickListener {
            val intent = Intent(this, Tus_Comunidades::class.java)
            startActivity(intent)
        }
    }
}
