package com.example.myinstagram

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register.setOnClickListener {
            val intent = Intent(this, EmailSignUpActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val username = user_name_inputbox.text.toString()
            val password = password_inputbox.text.toString()

            (application as MasterApplication).service.login(
                username, password
            ).enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val token = user!!.token!!
                        saveUserToken(token, this@LoginActivity)
                        (application as MasterApplication).createRetrofit()

                        Toast.makeText(this@LoginActivity, "로그인 하셨습니다.", Toast.LENGTH_LONG)

                        startActivity(
                            Intent(this@LoginActivity, MyInstagramPostListActivity::class.java)
                        )
                    }
                }
            })
        }
    }

    fun saveUserToken(token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
}