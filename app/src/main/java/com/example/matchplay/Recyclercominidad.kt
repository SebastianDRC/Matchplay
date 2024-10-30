package com.example.matchplay

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Date

class Recyclercominidad : AppCompatActivity() {
    private val auth= FirebaseAuth.getInstance()
    private val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recyclercominidad)
        val post = Post(
            comuniName = "grupo",
            date = java.util.Date(),
            imagen = ContextCompat.getDrawable(this, R.drawable.basquetbol)
        )
        val posts= listOf(post)

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@Recyclercominidad)
            adapter = ControlPost(this@Recyclercominidad, posts)
    }
}}