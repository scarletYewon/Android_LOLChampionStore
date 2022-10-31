package com.kmu.mymp

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kmu.mymp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var auth : FirebaseAuth? = null
    var noMem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.noMem.setOnClickListener {
            noMem = true
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("data", noMem)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            signinEmail()
        }
        binding.signUp.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }
    // 로그인
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(binding.email.text.toString(),binding.pw.text.toString())
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful) {
                    // Login, 아이디와 패스워드가 맞았을 때
                    moveMainPage(task.result?.user)
                } else {
                    // Show the error message, 아이디와 패스워드가 틀렸을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        val am: AccountManager = AccountManager.get(this) // "this" references the current Context
        val accounts: Array<out Account> = am.getAccountsByType("com.google")
    }
    // 로그인이 성공하면 다음 페이지로 넘어가는 함수
    private fun moveMainPage(user: FirebaseUser?) {
        // 파이어베이스 유저 상태가 있을 경우 다음 페이지로 넘어갈 수 있음
        if(user != null) {
            val intent = (Intent(this, ThirdActivity::class.java))
            startActivity(intent)
        }
    }
}