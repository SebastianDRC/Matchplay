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
            val createButton = findViewById<Button>(R.id.button)

            createButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val sport = sportEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (name.isNotEmpty() && sport.isNotEmpty() && description.isNotEmpty()) {
                    Toast.makeText(this, "Comunidad creada con éxito", Toast.LENGTH_SHORT).show()
                    // Aquí puedes agregar lógica adicional para guardar la información o enviar a otro lugar
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
        }
