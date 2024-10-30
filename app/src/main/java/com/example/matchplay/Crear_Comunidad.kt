package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
class Crear_Comunidad : AppCompatActivity() {
    val pickmedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.i("aris","seleccionado")
            //imagenseleccionada
        } else {
            Log.i("aris","No seleccionado")
            //no imagen

        }
    }
    lateinit var buttonimge: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_comunidad)
        buttonimge = findViewById(R.id.imageButton)
        buttonimge.setOnClickListener {
            pickmedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        val button = findViewById<ImageButton>(R.id.iniciarlogin)

        button.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)

        }
        val nameEditText = findViewById<EditText>(R.id.editTextText1)
        val sportEditText = findViewById<EditText>(R.id.editTextText2)
        val descriptionEditText = findViewById<EditText>(R.id.editTextText3)
        val createButton = findViewById<Button>(R.id.button2)

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val sport = sportEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (name.isNotEmpty() && sport.isNotEmpty() && description.isNotEmpty()) {
                Log.d(
                    "MainActivity",
                    "Datos: Nombre=$name, Deporte=$sport, Descripción=$description"
                )

                val intent = Intent(this, Tus_Comunidades::class.java)
                intent.putExtra("EXTRA_NAME", name)
                intent.putExtra("EXTRA_SPORT", sport)
                intent.putExtra("EXTRA_DESCRIPTION", description)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}