package com.kmu.mymp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kmu.mymp.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        var a = intent.getBooleanExtra("data",false)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_third)

        auth = Firebase.auth
        binding.myInfo.setOnClickListener {
            memberOrNot(a)
        }
    }
    private fun memberOrNot(a: Boolean) {
        if(a == false) {
            var b = intent.getStringExtra("mail")
            var c = intent.getStringExtra("pw")
            val intent = (Intent(this, MyinfoActivity::class.java))
            intent.putExtra("mail", b)
            intent.putExtra("pw", c)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

}