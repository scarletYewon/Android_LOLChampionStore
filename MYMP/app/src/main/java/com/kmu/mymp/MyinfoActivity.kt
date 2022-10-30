package com.kmu.mymp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MyinfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)

        var b = intent.getStringExtra("mail")
        var c = intent.getStringExtra("pw")

        var em = findViewById<TextView>(R.id.emailText)
        em.setText(b)

        var p = findViewById<TextView>(R.id.pwText)
        p.setText(c)


    }




}
