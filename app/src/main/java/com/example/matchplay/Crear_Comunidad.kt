package com.example.matchplay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

class Crear_Comunidad : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var nameTextView: TextView
    private var selectedImageUri: Uri? = null

    // Configuración para seleccionar una imagen
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.i("SeleccionarImagen", "Imagen seleccionada: $uri")
            selectedImageUri = uri
            resizeAndUploadImage(uri) // Llama al método de redimensionamiento
        } else {
            Log.i("SeleccionarImagen", "No se seleccionó ninguna imagen")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_comunidad)

        // Inicializar Firebase Auth, Firestore y Storage
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Habilitar persistencia offline para Firestore
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        // Configuración para botón de selección de imagen
        val buttonImage: ImageButton = findViewById(R.id.fotoComunidad)
        buttonImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                Log.d("MainActivity", "Datos: Nombre=$name, Deporte=$sport, Descripción=$description")
                val intent = Intent(this, Tus_Comunidades::class.java)
                intent.putExtra("EXTRA_NAME", name)
                intent.putExtra("EXTRA_SPORT", sport)
                intent.putExtra("EXTRA_DESCRIPTION", description)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para redimensionar y subir la imagen a Firebase Storage
    private fun resizeAndUploadImage(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 250, 250, true)

            val baos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            val imageRef = storage.reference.child("profile_images/$userId/${UUID.randomUUID()}.jpg")
            val uploadTask = imageRef.putBytes(imageData)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveImageUrlToFirestore(downloadUri.toString())
                }
            }.addOnFailureListener { e ->
                Log.e("UploadImage", "Error al subir la imagen", e)
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ResizeAndUploadImage", "Error al redimensionar o subir la imagen", e)
            Toast.makeText(this, "Error al redimensionar o subir la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para guardar la URL de la imagen en Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show()
                loadProfileImage()
            }
            .addOnFailureListener { e ->
                Log.e("SaveImageUrl", "Error al guardar la URL de la imagen", e)
                Toast.makeText(this, "Error al guardar la URL de la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para cargar la imagen en el botón desde Firestore
    private fun loadProfileImage() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val imageUrl = document.getString("profileImageUrl")
                if (imageUrl != null) {
                    val buttonImage: ImageButton = findViewById(R.id.fotoComunidad)
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .into(buttonImage)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar la imagen de perfil", Toast.LENGTH_SHORT).show()
                Log.e("LoadProfileImage", "Error al obtener imagen de Firestore", e)
            }
    }
}
