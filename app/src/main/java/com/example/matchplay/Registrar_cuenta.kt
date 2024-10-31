package com.example.matchplay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class Registrar_cuenta : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cuenta)

        val return_main_btn = findViewById<ImageButton>(R.id.return_main)
        val create_btn = findViewById<Button>(R.id.crear_cuenta)
        val emailEditText = findViewById<EditText>(R.id.email_et)
        val passwordEditText = findViewById<EditText>(R.id.password_et)
        val nombreEditText = findViewById<EditText>(R.id.nombre)

        create_btn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nombre = nombreEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(nombre)
                        .build()

                    authResult.user!!.updateProfile(profile)
                        .addOnSuccessListener {
                            // Guardar los datos en Firestore
                            val userId = authResult.user!!.uid
                            val userMap = hashMapOf(
                                "name" to nombre,
                                "email" to email,
                                "password" to password
                            )

                            db.collection("users").document(userId)
                                .set(userMap)
                                .addOnSuccessListener {
                                    AlertDialog.Builder(this).apply {
                                        setTitle("Cuenta Creada")
                                        setMessage("Tu cuenta ha sido creada correctamente")
                                        setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                            finish()  // Cierra la actividad actual
                                        }
                                    }.show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al actualizar el perfil: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al crear la cuenta: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        return_main_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
