package com.example.matchplay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class Registrar_cuenta : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cuenta)
        val return_main_btn = findViewById<ImageButton>(R.id.return_main)
        val create_btn = findViewById<Button>(R.id.crear_cuenta)
        val email = findViewById<EditText>(R.id.email_et)
        val password = findViewById<EditText>(R.id.password_et)
        val nombre = findViewById<EditText>(R.id.nombre)
        create_btn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val nombre = nombre.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(nombre)
                        .build()

                    it.user!!.updateProfile(profile)
                        .addOnSuccessListener {
                            AlertDialog.Builder(this).apply {
                                setTitle("Cuenta Creada")
                                setMessage("Tu cuenta ha sido creada correctamente")
                                setPositiveButton("aceptar"){ _: DialogInterface, _: Int ->
                                    finish()
                                }
                            }.show()
                        }
                        .addOnFailureListener {
                            Utils.showError(this,it.message.toString())
                        }
                }
                .addOnFailureListener {
                    Utils.showError(this,it.message.toString())
                }
        }
        return_main_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}