package com.example.myinstagram

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.activity_mytube.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MytubeActivity : AppCompatActivity() {

    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytube)

        (application as MasterApplication).service.getYoutubeList()
            .enqueue(object : Callback<ArrayList<Youtube>> {
                override fun onResponse(
                    call: Call<ArrayList<Youtube>>,
                    response: Response<ArrayList<Youtube>>
                ) {
                    if (response.isSuccessful) {
                        glide = Glide.with(this@MytubeActivity)
                        val youtubeList = response.body()
                        val adapter = MytubeAdapter(
                            youtubeList!!,
                            LayoutInflater.from(this@MytubeActivity),
                            glide,
                            this@MytubeActivity
                        )
                        youtube_list_recycler.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ArrayList<Youtube>>, t: Throwable) {
                }
            })
    }
}

class MytubeAdapter(
    var youtubeList: ArrayList<Youtube>,
    var inflater: LayoutInflater,
    val glide: RequestManager,
    val activity: Activity

) : RecyclerView.Adapter<MytubeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val thumbnail: ImageView
        val content: TextView

        init {
            title = itemView.findViewById(R.id.youtube_title)
            thumbnail = itemView.findViewById(R.id.youtube_thumbnail)
            content = itemView.findViewById(R.id.youtube_content)

            itemView.setOnClickListener {
                val position = adapterPosition
                val intent = Intent(activity, MytubeDetailActivity::class.java)
                intent.putExtra("video_url", youtubeList.get(position).video)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.youtube_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(youtubeList.get(position).title)
        holder.content.setText(youtubeList.get(position).content)
        glide.load(youtubeList.get(position).thumbnail).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return youtubeList.size
    }
}
