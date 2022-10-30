package com.kmu.mymp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kmu.mymp.databinding.ActivitySecondBinding
import java.lang.Character.isDigit


class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private var auth : FirebaseAuth? = null

    private fun createAccount(newId: String, newPw: String, name: String, phone: String, address: String) {
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
        if (newId.isNotEmpty() && newPw.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && num == true && eng == true) {
            auth?.createUserWithEmailAndPassword(newId, newPw)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
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
            createAccount(binding.newId.text.toString(),binding.newPw.text.toString(),binding.name.text.toString(),binding.number.text.toString(),binding.address.text.toString())
        }
        val show_Btn = findViewById<Button>(R.id.show_Btn)
        show_Btn.setOnClickListener {
            showPopup()
        }
    }
    private fun showPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("개인정보 약관 동의")
        builder.setMessage("<개인정보보호 포털> 개인정보 처리방침\n" +
                "개인정보보호위원회가 취급하는 모든 개인정보는 관련 법령에 근거하여 수집 · 보유 및 처리되고 있습니다. 「개인정보보호법」은 이러한 개인정보의 취급에 대한 일반적 규범을 제시하고 있으며, 개인정보보호위원회는 이러한 법령의 규정에 따라 수집 · 보유 및 처리하는 개인정보를 공공업무의 적절한 수행과 이용자의 권익을 보호하기 위해 적법하고 적정하게 취급할 것입니다.\n" +
                "\n" +
                "또한, 개인정보보호위원회는 관련 법령에서 규정한 바에 따라 보유하고 있는 개인정보에 대한 열람, 정정·삭제, 처리정지 요구 등 이용자의 권익을 존중하며, 이용자는 이러한 법령상 권익의 침해 등에 대하여 행정심판법에서 정하는 바에 따라 행정심판을 청구할 수 있습니다.\n" +
                "\n" +
                "개인정보보호위원회는 개인정보보호법 제 30조에 따라 정보주체의 개인정보 보호 및 권익을 보호하고 개인정보와 관련한 이용자의 고충을 원활하게 처리할 수 있도록 다음과 같은 개인정보 처리방침을 수립 · 공개하고 있습니다.")
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