package com.example.matchplay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

class Perfil : AppCompatActivity() {
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
        setContentView(R.layout.activity_perfil)

        // Inicializar Firebase Auth, Firestore y Storage
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Habilitar persistencia offline para Firestore
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        // Inicializar TextView donde se mostrará el nombre
        nameTextView = findViewById(R.id.nameTextView)

        // Cargar el nombre de usuario desde Firestore
        loadUserName()

        // Configuración para botón de selección de imagen
        val buttonImage: ImageButton = findViewById(R.id.seleccindeImagen)
        buttonImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Cargar la imagen de perfil en el ImageButton al iniciar el perfil
        loadProfileImage()

        // Configuración para botón de cerrar sesión
        findViewById<ImageButton>(R.id.imageButtoncerrar_sesion).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Configuración para botón de retroceso
        findViewById<ImageButton>(R.id.flechaRetroceder).setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
            finish()
        }

        // Configuración para botón de eliminar cuenta
        findViewById<ImageButton>(R.id.eliminarCuenta).setOnClickListener {
            auth.currentUser?.let { deleteAccount(it) }
        }
    }

    private fun loadUserName() {
        auth.currentUser?.uid?.let { userId ->
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nameTextView.text = "Bienvenido, ${document.getString("name")}"
                    } else {
                        Toast.makeText(this, "No se encontró el perfil", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al obtener el perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Método para redimensionar y subir la imagen a Firebase Storage
    private fun resizeAndUploadImage(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return

        try {
            // Obtener el input stream de la imagen seleccionada
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // Redimensionar la imagen a un tamaño máximo de 250x250 píxeles
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 250, 250, true)

            // Convertir el Bitmap redimensionado en un array de bytes
            val baos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            // Subir la imagen a Firebase Storage
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
                // Cargar la imagen en el botón después de actualizarla
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
                    // Usa Glide para cargar la imagen en el ImageButton
                    val buttonImage: ImageButton = findViewById(R.id.seleccindeImagen)
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image) // Imagen de placeholder
                        .into(buttonImage)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar la imagen de perfil", Toast.LENGTH_SHORT).show()
                Log.e("LoadProfileImage", "Error al obtener imagen de Firestore", e)
            }
    }

    // Función para eliminar la cuenta del usuario y su documento en Firestore
    private fun deleteAccount(user: FirebaseUser) {
        val userId = user.uid
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
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