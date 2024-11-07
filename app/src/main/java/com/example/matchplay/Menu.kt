package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class Menu :  AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val cardViewCommunity = findViewById<CardView>(R.id.cardViewCommunity)
        val tvCommunityName = findViewById<TextView>(R.id.textView122)
        val tvSport = findViewById<TextView>(R.id.textView)
        val tvUsername = findViewById<TextView>(R.id.textView3)
        val postBackgroundImage = findViewById<ImageView>(R.id.postBackgroundImage)

        val communityName = intent.getStringExtra("COMMUNITY_NAME")
        val sport = intent.getStringExtra("SPORT")
        val username = intent.getStringExtra("USERNAME")
        val imageUrl = intent.getStringExtra("IMAGE_URL")

        if (!communityName.isNullOrEmpty() && !sport.isNullOrEmpty() && !username.isNullOrEmpty() && !imageUrl.isNullOrEmpty()) {
            tvCommunityName.text = communityName
            tvSport.text = sport
            tvUsername.text = username

            Glide.with(this)
                .load(imageUrl)
                .into(postBackgroundImage)
            Log.i("Crear_imagen", "el valor de imagen  $imageUrl", )
            cardViewCommunity.visibility = CardView.VISIBLE
        } else {
            cardViewCommunity.visibility = CardView.GONE
        }

        val buttonPerfil = findViewById<ImageButton>(R.id.perfilbotton)
        buttonPerfil?.setOnClickListener {
            startActivity(Intent(this, Perfil::class.java))
        }

        val buttonCrearComunidad = findViewById<ImageButton>(R.id.imageButton7)
        buttonCrearComunidad?.setOnClickListener {
            startActivity(Intent(this, Crear_Comunidad::class.java))
        }

        val buttonComunidades = findViewById<ImageButton>(R.id.tusComunidades)
        buttonComunidades?.setOnClickListener {
            startActivity(Intent(this, Tus_Comunidades::class.java))
        }

        val buttonBusqueda = findViewById<ImageButton>(R.id.imageButton8)
        buttonBusqueda?.setOnClickListener {
            startActivity(Intent(this, Busqueda::class.java))
        }
    }
}