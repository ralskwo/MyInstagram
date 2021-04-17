package com.example.myinstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.activity_my_instagram_post_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInstagramPostListActivity : AppCompatActivity() {

    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_instagram_post_list)

        glide = Glide.with(this)

        (application as MasterApplication).service.getAllposts().enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if (response.isSuccessful) {
                        val postList = response.body()
                        val adapter = PostAdapter(
                            postList!!,
                            LayoutInflater.from(this@MyInstagramPostListActivity),
                            glide
                        )
                        post_recyclerview.adapter = adapter
                        post_recyclerview.layoutManager =
                            LinearLayoutManager(this@MyInstagramPostListActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

        menu_btn.setOnClickListener { startActivity(Intent(this,MyInstagramUserInfo::class.java))}
        MyInstagram.setOnClickListener { startActivity(Intent(this,MyInstagramPostListActivity::class.java))}
        Youtube.setOnClickListener { startActivity(Intent(this,MytubeActivity::class.java))}
        Melon.setOnClickListener {startActivity(Intent(this,MelonActivity::class.java))}
    }

}

class PostAdapter(
    var postList: ArrayList<Post>,
    var inflater: LayoutInflater,
    val glide: RequestManager

) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postOwner: TextView
        val postImage: ImageView
        val postContent: TextView

        init {
            postOwner = itemView.findViewById(R.id.post_owner)
            postImage = itemView.findViewById(R.id.post_img)
            postContent = itemView.findViewById(R.id.post_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.myinstagram_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postOwner.setText(postList.get(position).owner)
        holder.postContent.setText(postList.get(position).content)
        glide.load(postList.get(position).image).into(holder.postImage)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
