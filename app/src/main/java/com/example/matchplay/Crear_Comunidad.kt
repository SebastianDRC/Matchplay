package com.example.matchplay


import android.app.ProgressDialog
import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
class Crear_Comunidad : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_comunidad)

        val etCommunityName = findViewById<EditText>(R.id.nombre)
        val etSport = findViewById<EditText>(R.id.deporte)
        val etUsername = findViewById<EditText>(R.id.nombreAutor)
        val btnCreateCommunity = findViewById<Button>(R.id.crearComunidad)

        btnCreateCommunity.setOnClickListener {
            val communityName = etCommunityName.text.toString()
            val sport = etSport.text.toString()
            val username = etUsername.text.toString()

            val intent = Intent(this, Menu::class.java).apply {
                putExtra("COMMUNITY_NAME", communityName)
                putExtra("SPORT", sport)
                putExtra("USERNAME", username)
            }
            startActivity(intent)
        }
    }




   // private val firestore = FirebaseFirestore.getInstance()
    //private val storageReference = FirebaseStorage.getInstance().reference

    //override fun onCreate(savedInstanceState: Bundle?) {
     //   super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_crear_comunidad)

       // val button = findViewById<ImageButton>(R.id.iniciarlogin25)

       // button.setOnClickListener {
       //     val intent = Intent(this, Menu::class.java)
         //   startActivity(intent)

      //  }


    }//}
