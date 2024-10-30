package com.example.matchplay
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val login_btn = findViewById<Button>(R.id.login_btn)
        val email = findViewById<EditText>(R.id.email_et)
        val password = findViewById<EditText>(R.id.password_et)
        val buttonregistrar = findViewById<Button>(R.id.registrar_cuenta_btn)
        buttonregistrar.setOnClickListener {
            val intent = Intent(this, Registrar_cuenta::class.java)
            startActivity(intent)
        }
        login_btn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Utils.showError(this,it.message.toString())
                }
        }
    }
}