package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Perfil : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.i("SeleccionarImagen", "Imagen seleccionada: $uri")
            // Aquí puedes manejar la imagen seleccionada
        } else {
            Log.i("SeleccionarImagen", "No se seleccionó ninguna imagen")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)


        auth = FirebaseAuth.getInstance()

        // Configuración para botón de selección de imagen
        val buttonImage: ImageButton = findViewById(R.id.imageButton5)
        buttonImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Configuración para botón de cerrar sesión
        val buttonCerrarSesion: ImageButton = findViewById(R.id.imageButtoncerrar_sesion)
        buttonCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configuración para botón de retroceso
        val buttonAtras: ImageButton = findViewById(R.id.imageButton2)
        buttonAtras.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        // Configuración para botón de eliminar cuenta
        val deleteAccountButton: ImageButton = findViewById(R.id.imageButton3)
        deleteAccountButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                deleteAccount(user)
            } else {
                Toast.makeText(this, "No hay ningún usuario autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para eliminar la cuenta del usuario
    private fun deleteAccount(user: FirebaseUser) {
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                    // Redirige al usuario a la pantalla de inicio de sesión o cierra la aplicación
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("DeleteAccount", "Error al eliminar la cuenta", task.exception)
                    Toast.makeText(this, "Error al eliminar la cuenta. Por favor, reautentícate e inténtalo de nuevo.", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Clase vacía de LoginActivity

}
