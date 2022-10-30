package com.kmu.mymp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MyinfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
    }
    val users = FirebaseAuth.getInstance().currentUser
    var sample = findViewById<TextView>(R.id.emailText)
//    sample.setText("d")

}
