package com.example.matchplay
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttoniniciar_app = findViewById<Button>(R.id.iniciar_app)

        buttoniniciar_app.setOnClickListener{
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
        }
        val buttonregistrar = findViewById<Button>(R.id.registrar)

        buttonregistrar.setOnClickListener{
            val intent = Intent(this,Registrar_cuenta::class.java)
            startActivity(intent)
        }
        }
    }