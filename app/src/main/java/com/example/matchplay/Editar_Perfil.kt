package com.example.matchplay

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Editar_Perfil : AppCompatActivity() {


    val pickMedia = registerForActivityResult(PickVisualMedia()){ uri ->
        if(uri!=null){
            //Imagen Seleccionada
        }else{
            // No imagen 
        }


    }
    lateinit var btnImage:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_perfil)
        btnImage = findViewById(R.id.imageButton10)
        btnImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

    }
}