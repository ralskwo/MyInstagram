package com.example.myinstagram

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {

    lateinit var service: Retrofit

    override fun onCreate() {
        super.onCreate()

    }

    fun createRetrofit(){
        //인터셉터. 나가려는 통신을 인터셉트하여
        val header = Interceptor{
            // original에 통신을 붙잡아 둔 후
            val original = it.request()
            // newBuilder를 사용하여 통신을 커스텀. 여기선 헤더를 붙임
            val request = original.newBuilder()
                .header("Authorization", "")
                .build()

            // 커스텀한 통신을 내보냄
            it.proceed(request)
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

        service = retrofit.create(Retrofit::class.java)
    }
}