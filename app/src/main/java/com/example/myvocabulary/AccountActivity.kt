package com.example.myvocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        var img=findViewById<ImageView>(R.id.img)
        img.setImageResource(R.drawable.profile)
    }
}