package com.example.myinstagram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_instagram_user_info.*

class MyInstagramUserInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_instagram_user_info)

        menu_btn.setOnClickListener { startActivity(Intent(this,MyInstagramUserInfo::class.java))}
        MyInstagram.setOnClickListener { startActivity(Intent(this,MyInstagramPostListActivity::class.java))}
        Youtube.setOnClickListener { startActivity(Intent(this,MytubeActivity::class.java))}
        Melon.setOnClickListener {startActivity(Intent(this,MelonActivity::class.java))}

        all_list.setOnClickListener { startActivity(Intent(this,MyInstagramPostListActivity::class.java))}
        my_list.setOnClickListener { startActivity(Intent(this, MyInstagramMyPostListActivity::class.java)) }
        upload.setOnClickListener { startActivity(Intent(this, MyInstagramUploadActivity::class.java)) }
        logout.setOnClickListener {
            val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("login_sp", "null")
            editor.commit()
            (application as MasterApplication).createRetrofit()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}