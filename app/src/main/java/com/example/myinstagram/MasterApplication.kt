package com.example.myinstagram

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {

    lateinit var service: RetrofitService

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        createRetrofit()
        // chrome://inspect/#devices
    }

    fun createRetrofit() {
        //인터셉터. 나가려는 통신을 인터셉트하여
        val header = Interceptor {
            // original에 통신을 붙잡아 둔 후
            val original = it.request()

            // 로그인을 했다면
           if (checkIsLogin()) {
               // ~~?.let : ~~이 null이 아니라면 let 이하를 수행
                getUserToken()?.let {token ->
                    // newBuilder를 사용하여 통신을 커스텀. 여기선 헤더를 붙임
                    val request = original.newBuilder()
                        .header("Authorization", "token " + token)
                        .build()
                    // 커스텀한 통신을 내보냄
                    it.proceed(request)
                }
            } else {
                // 커스텀한 통신을 내보냄
                it.proceed(original)
            }

        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(RetrofitService::class.java)


    }

    fun checkIsLogin(): Boolean {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")

        if (token != "null") return true
        else return false
    }

    fun getUserToken(): String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")

        if (token == "null") return null
        else return token
    }


}