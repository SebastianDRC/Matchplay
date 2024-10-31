package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        // Obtener referencias a los TextViews
        val cardViewCommunity = findViewById<CardView>(R.id.cardViewCommunity)
        val tvCommunityName = findViewById<TextView>(R.id.textView122)
        val tvSport = findViewById<TextView>(R.id.textView)
        val tvUsername = findViewById<TextView>(R.id.textView3)

        // Obtener los datos del Intent y asignar un valor predeterminado si es nulo
        val communityName = intent.getStringExtra("COMMUNITY_NAME")
        val sport = intent.getStringExtra("SPORT")
        val username = intent.getStringExtra("USERNAME")

        if (!communityName.isNullOrEmpty() && !sport.isNullOrEmpty() && !username.isNullOrEmpty()) {
        // Establecer los valores en los TextViews
        tvCommunityName.text = "$communityName"
        tvSport.text = "$sport"
        tvUsername.text = "$username"

        // Mostrar el CardView
        cardViewCommunity.visibility = CardView.VISIBLE
    } else
    {
        // Mantener el CardView oculto si los datos son nulos o vacíos
        cardViewCommunity.visibility = CardView.GONE


        // val tvCommunityDetails = findViewById<TextView>(R.id.tvCommunityDetails)

        // Obtener los datos del Intent
        //val communityName = intent.getStringExtra("COMMUNITY_NAME")
        //val sport = intent.getStringExtra("SPORT")
        //val username = intent.getStringExtra("USERNAME")

        // Mostrar los detalles en el TextView
        //val details = "Comunidad: $communityName\nDeporte: $sport\nUsuario: $username"
        //tvCommunityDetails.text = details
        //  }


        //override fun onCreate(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        //setContentView(R.layout.activity_menu)
    }
    val buttonPerfil = findViewById<ImageButton>(R.id.perfilbotton)
    buttonPerfil?.setOnClickListener()
    {
        val intent = Intent(this, Perfil::class.java)
        startActivity(intent)
    }

    val buttonCrearComunidad = findViewById<ImageButton>(R.id.imageButton7)
    buttonCrearComunidad?.setOnClickListener()
    {
        val intent = Intent(this, Crear_Comunidad::class.java)
        startActivity(intent)
    }

    val buttonComunidades = findViewById<ImageButton>(R.id.crearcomunidades)
    buttonComunidades?.setOnClickListener()
    {
        val intent = Intent(this, Tus_Comunidades::class.java)
        startActivity(intent)
    }

    val buttonBusqueda = findViewById<ImageButton>(R.id.imageButton8)
    buttonBusqueda?.setOnClickListener()
    {
        val intent = Intent(this, Busqueda::class.java)
        startActivity(intent)
    }
}}