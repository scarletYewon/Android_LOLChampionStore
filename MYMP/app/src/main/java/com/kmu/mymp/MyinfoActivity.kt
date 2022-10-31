package com.kmu.mymp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyinfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)

        val mail = findViewById<TextView>(R.id.emailText)
        val name = findViewById<TextView>(R.id.nameText)
        val number = findViewById<TextView>(R.id.numberText)
        val address = findViewById<TextView>(R.id.addressText)
        auth = Firebase.auth
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        userDB = Firebase.database.reference
        val currentUserDB = userDB.child(getCurrentUserId())

        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mail.text = snapshot.child("newId").value.toString()
                name.text = snapshot.child("name").value.toString()
                number.text = snapshot.child("number").value.toString()
                address.text = snapshot.child("address").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun getCurrentUserId(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }
}
