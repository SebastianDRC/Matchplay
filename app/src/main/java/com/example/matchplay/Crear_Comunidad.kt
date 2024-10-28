package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
class Crear_Comunidad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_comunidad)
        val button= findViewById<ImageButton>(R.id.iniciarlogin)

        button.setOnClickListener{
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)

            val nameEditText = findViewById<EditText>(R.id.editTextText1)
            val sportEditText = findViewById<EditText>(R.id.editTextText2)
            val descriptionEditText = findViewById<EditText>(R.id.editTextText3)

                }
            }
        }

