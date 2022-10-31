package com.kmu.mymp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kmu.mymp.databinding.ActivitySecondBinding
import java.lang.Character.isDigit


class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private var auth : FirebaseAuth? = null

    private fun createAccount(newId: String, newPw: String, name: String, phone: String, address: String, accept: Boolean) {
        var eng = false
        var num = false
        for (i: Int in 0 until newPw.length) {
            if (isDigit(newPw.get(i))) {
                num = true
            } else if (0x61.toChar() <= newPw.get(i) && newPw.get(i) <= 0x7A.toChar() || 0x41.toChar() <= newPw.get(
                    i
                ) && newPw.get(i) <= 0x5A.toChar()
            ) {
                eng = true
            }
            if (eng && num) {
                break
            }
        }
        if (newId.isNotEmpty() && newPw.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && accept == true && num == true && eng == true) {
            auth?.createUserWithEmailAndPassword(newId, newPw)
                ?.addOnCompleteListener(this) { task ->
                    val uid = FirebaseAuth.getInstance().uid ?:""
                    if (task.isSuccessful) {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference()
                        val dataInput = Users(
                            binding.newId.text.toString(),
                            binding.newPw.text.toString(),
                            binding.name.text.toString(),
                            binding.number.text.toString(),
                            binding.address.text.toString(),
                            binding.accept.isChecked
                        )
                        myRef.child(uid).setValue(dataInput)
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()

                        moveMainPage(task.result?.user)
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else if (eng == false or num == false){
            Toast.makeText(
                this, "영어와 숫자를 포함하여 6자리 이상 입렵해주세요",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (accept == false){
            Toast.makeText(
                this, "개인정보 수집 및 이용에 동의해주세요",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            Toast.makeText(
                this, "모든 칸을 채워주새요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
        binding.save.setOnClickListener{
            createAccount(binding.newId.text.toString(),binding.newPw.text.toString(),binding.name.text.toString(),binding.number.text.toString(),binding.address.text.toString(),binding.accept.isChecked)
        }
        val show_Btn = findViewById<Button>(R.id.show_Btn)
        show_Btn.setOnClickListener {
            showPopup()
        }
    }
    private fun showPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("개인정보 수집 및 이용 안내")
        builder.setMessage("<개인정보보호 포털> 개인정보 처리방침\n" +
                "회원가입을 위해서는 아래와 같이 개인정보를 수집 및 이용합니다.\n" +
                "\n" +
                "1. 개인정보 수집 목적: 회원관리\n" +
                "\n" +
                "2. 개인정보 수집 항목 : 이메일, 비밀번호, 이름, 전화번호, 주소\n" +
                "\n" +
                "3. 보유 및 이용기간: 회원 탈퇴 시까지\n" +
                "\n" +
                "위 개인정보 수집 및 이용을 확인합니다.")
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i:Int->}
        builder.show()
    }
    // 로그인이 성공하면 다음 페이지로 넘어가는 함수
    fun moveMainPage(user: FirebaseUser?) {
        // 파이어베이스 유저 상태가 있을 경우 다음 페이지로 넘어갈 수 있음
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}