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
import android.widget.ImageView
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

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    // Configuración para seleccionar una imagen
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            selectedImageUri = uri
            Glide.with(this).load(uri).into(findViewById(R.id.fotoComunidad)) // Muestra la imagen seleccionada en el botón
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_comunidad)

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

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
                selectedImageUri?.let {
                    uploadImageAndSaveCommunity(it, communityName, sport, username)
                } ?: Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageAndSaveCommunity(uri: Uri, communityName: String, sport: String, username: String) {
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Creando comunidad...")
            setCancelable(false)
            show()
        }

        val imageRef = storage.reference.child("community_images/${UUID.randomUUID()}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveCommunityToFirestore(communityName, sport, username, downloadUri.toString())
                    progressDialog.dismiss()
                    startActivity(Intent(this, Menu::class.java)) // Ir al menú después de guardar
                }.addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show()
                    Log.e("Crear_Comunidad", "Error al obtener la URL de la imagen", e)
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                Log.e("Crear_Comunidad", "Error al subir la imagen", e)
            }
    }

    private fun saveCommunityToFirestore(name: String, sport: String, username: String, imageUrl: String) {
        val communityData = hashMapOf(
            "name" to name,
            "sport" to sport,
            "username" to username,
            "imageUrl" to imageUrl
        )

        db.collection("communities").add(communityData)
            .addOnSuccessListener {
                Toast.makeText(this, "Comunidad creada con éxito", Toast.LENGTH_SHORT).show()
                // Navegar al menú después de guardar
                val intent = Intent(this, Menu::class.java).apply {
                    putExtra("COMMUNITY_NAME", name)
                    putExtra("SPORT", sport)
                    putExtra("USERNAME", username)
                    putExtra("IMAGE_URL", imageUrl)
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al crear la comunidad", Toast.LENGTH_SHORT).show()
                Log.e("Crear_Comunidad", "Error al guardar en Firestore", e)
            }
    }
}
