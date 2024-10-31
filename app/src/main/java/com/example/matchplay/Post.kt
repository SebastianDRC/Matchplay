package com.example.matchplay

import android.graphics.drawable.Drawable
import com.google.firebase.firestore.Exclude
import java.util.Date

class Post(val comuniName: String? = null, val date: Date?= null,val username: String? = null, val miembronuevo: ArrayList<String>? = arrayListOf()) {
    @Exclude
    @set: Exclude
    @get:Exclude
    var uid: String?= null

    constructor():this(null,null,null,null)
}
