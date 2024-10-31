package com.example.matchplay

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    private var selectedImageUri: Uri? = null

    // Configuración para seleccionar una imagen
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            Log.i("SeleccionarImagen", "Imagen seleccionada: $uri")
            selectedImageUri = uri
            resizeAndUploadImage(uri)
        } ?: Log.i("SeleccionarImagen", "No se seleccionó ninguna imagen")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_comunidad)

        initializeFirebase()
        val volveralInicio = findViewById<ImageButton>(R.id.iniciarlogin)
        volveralInicio?.setOnClickListener()
        {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        val etCommunityName = findViewById<EditText>(R.id.nombre)
        val etSport = findViewById<EditText>(R.id.deporte)
        val etUsername = findViewById<EditText>(R.id.nombreAutor)
        val btnCreateCommunity = findViewById<Button>(R.id.crearComunidad)
        val buttonImage: ImageButton = findViewById(R.id.fotoComunidad)

        buttonImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnCreateCommunity.setOnClickListener {
            val communityName = etCommunityName.text.toString().trim()
            val sport = etSport.text.toString().trim()
            val username = etUsername.text.toString().trim()

            if (communityName.isNotEmpty() && sport.isNotEmpty() && username.isNotEmpty()) {
                createCommunity(communityName, sport, username)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
        storage = FirebaseStorage.getInstance()
    }

    // Método para redimensionar y subir la imagen a Firebase Storage
    private fun resizeAndUploadImage(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Subiendo imagen...")
            setCancelable(false)
            show()
        }

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 250, 250, true)

            val baos = ByteArrayOutputStream().apply {
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            }
            val imageData = baos.toByteArray()

            val imageRef = storage.reference.child("community_images/$userId/${UUID.randomUUID()}.jpg")
            val uploadTask = imageRef.putBytes(imageData)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveImageUrlToFirestore(downloadUri.toString())
                }
                progressDialog.dismiss()
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.e("UploadImage", "Error al subir la imagen", e)
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            progressDialog.dismiss()
            Log.e("ResizeAndUploadImage", "Error al redimensionar o subir la imagen", e)
            Toast.makeText(this, "Error al redimensionar o subir la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para guardar la URL de la imagen en Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("communityImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen de la comunidad actualizada", Toast.LENGTH_SHORT).show()
                loadProfileImage(imageUrl)
            }
            .addOnFailureListener { e ->
                Log.e("SaveImageUrl", "Error al guardar la URL de la imagen", e)
                Toast.makeText(this, "Error al guardar la URL de la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para cargar la imagen en el botón desde Firestore
    private fun loadProfileImage(imageUrl: String) {
        val buttonImage: ImageButton = findViewById(R.id.fotoComunidad)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(buttonImage)
    }

    // Función para crear la comunidad y guardar los datos en Firestore
    private fun createCommunity(name: String, sport: String, username: String) {
        val communityData = mapOf(
            "name" to name,
            "sport" to sport,
            "username" to username,
            "imageUrl" to selectedImageUri.toString() // Adding the selected image URI as a placeholder
        )

        db.collection("communities")
            .add(communityData)
            .addOnSuccessListener {
                Toast.makeText(this, "Comunidad creada con éxito", Toast.LENGTH_SHORT).show()
                Intent(this, Menu::class.java).apply {
                    putExtra("COMMUNITY_NAME", name)
                    putExtra("SPORT", sport)
                    putExtra("USERNAME", username)
                }.also { startActivity(it) }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al crear la comunidad: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("CreateCommunity", "Error al guardar datos en Firestore", e)
            }
    }
}
