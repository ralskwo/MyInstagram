package com.example.myinstagram

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_my_instagram_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyInstagramUploadActivity : AppCompatActivity() {

    lateinit var filePath:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_instagram_upload)

        view_pictures.setOnClickListener {
            getPicture()
        }
        upload_post.setOnClickListener {
            uploadPost()
        }

        menu_btn.setOnClickListener { startActivity(Intent(this,MyInstagramUserInfo::class.java))}
        MyInstagram.setOnClickListener { startActivity(Intent(this,MyInstagramPostListActivity::class.java))}
        Youtube.setOnClickListener { startActivity(Intent(this,MytubeActivity::class.java))}
        Melon.setOnClickListener {startActivity(Intent(this,MelonActivity::class.java))}

    }

    // 사진을 가져오는 것은 내 어플을 벗어나는 것이다. 그래서 인텐트를 보내주어야 한다
    fun getPicture(){

        val intent = Intent(Intent.ACTION_PICK)
        // 외부 저장소로 가겠다는 뜻이다.
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 그 중에서 이미지만 가져오도록 한다.
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            // uri = 어떤 자료의 위치 (url의 상위 개념)
            // url = 어떤 웹 주소의 위치
            val uri: Uri = data!!.data!!
            filePath = getImageFilePath(uri)
        }
    }

    // 파일의 절대경로를 얻기 위한 함수
    fun getImageFilePath(contentUri :Uri):String{
        var columnIndex = 0
        // = 이하를 걸러내기 위한 틀.
        val projection =  arrayOf(MediaStore.Images.Media.DATA)
        // 커서는 어떤 인덱스를 가리키기 위한 것이라고 생각하면 쉽다.
        // contentResolver : 컨텐트를 관리하는
        // 상대경로를 query에 넣어서 절대경로를 얻고자 하는 줄이다.
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        // 커서를 첫 번째로 이동시키고
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    fun uploadPost(){
        val file = File(filePath)
        // MediaType.parse : 타입을 정해주는 함수. 여기서는 image로 정함
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        // 데이터를 보낼 때 한번에 보내는것이 아니라 여러 개로 쪼개어 보내기 떄문에 multipartbody를 사용한다.
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/plain"), getContent())

        (application as MasterApplication).service.uploadPost(
            part, content
        ).enqueue(object :Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {

                if(response.isSuccessful){
                    finish()
                    startActivity(Intent(this@MyInstagramUploadActivity, MyInstagramMyPostListActivity::class.java))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                TODO("Not yet implemented")
            }
        } )
    }

    fun getContent():String{
        return content_input.text.toString()
    }
}