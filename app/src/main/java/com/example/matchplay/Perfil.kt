package com.example.matchplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class Perfil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nameTextView: TextView

    // Configuración para seleccionar una imagen
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.i("SeleccionarImagen", "Imagen seleccionada: $uri")
        } else {
            Log.i("SeleccionarImagen", "No se seleccionó ninguna imagen")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar Firebase Auth y Firestore
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        auth = FirebaseAuth.getInstance()

        // Inicializar TextView donde se mostrará el nombre
        nameTextView = findViewById(R.id.nameTextView)

        // Obtener ID de usuario actual y recuperar el nombre desde Firestore
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        nameTextView.text = "Bienvenido, $name"
                    } else {
                        Toast.makeText(this, "No se encontró el perfil", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al obtener el perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("Perfil", "El usuario no está autenticado")
        }

        // Configuración para botón de selección de imagen
        val buttonImage: ImageButton = findViewById(R.id.imageButton5)
        buttonImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Configuración para botón de cerrar sesión
        val buttonCerrarSesion: ImageButton = findViewById(R.id.imageButtoncerrar_sesion)
        buttonCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configuración para botón de retroceso
        val buttonAtras: ImageButton = findViewById(R.id.imageButton2)
        buttonAtras.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
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

    // Función para eliminar la cuenta del usuario y su documento en Firestore
    private fun deleteAccount(user: FirebaseUser) {
        val userId = user.uid

        // Primero, intenta eliminar el documento del usuario en Firestore
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                Log.d("DeleteAccount", "Documento del usuario eliminado de Firestore")

                // Luego, elimina la cuenta de autenticación del usuario
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("DeleteAccount", "Error al eliminar la cuenta", task.exception)
                            Toast.makeText(this, "Error al eliminar la cuenta. Por favor, reautentícate e inténtalo de nuevo.", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("DeleteAccount", "Error al eliminar el documento en Firestore", e)
                Toast.makeText(this, "Error al eliminar los datos del perfil en Firestore", Toast.LENGTH_SHORT).show()
            }
    }
}
