package com.kmu.mymp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kmu.mymp.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_third)
        binding.myInfo.setOnClickListener{
            val intent = Intent(this, MyinfoActivity::class.java)
            startActivity(intent)
        }
    }


}