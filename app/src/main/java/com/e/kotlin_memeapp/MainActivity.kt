package com.e.kotlin_memeapp

import android.content.Intent
import android.graphics.drawable.Drawable


import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var urlString:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
    }
    fun loadData(){


        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener<JSONObject> { response ->
                var imageView:ImageView=findViewById(R.id.memeImage)
                urlString=response.getString("url")
                var progress:ProgressBar=findViewById(R.id.progressBar)

                Glide.with(this).load(urlString).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility=View.GONE


                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        progress.visibility=View.GONE

                        return false
                    }
                })
                    .into(imageView);
            },
            Response.ErrorListener {
                Log.d("VolleyResponse","Error Occured")
                var image:ImageView=findViewById(R.id.memeImage)
                var progress:ProgressBar=findViewById(R.id.progressBar)
                progress.visibility=View.GONE

            })

       // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey check this meme $urlString")

            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share this meme using ...")
        startActivity(shareIntent)

    }

    fun nextMeme(view: View) {
        var progress:ProgressBar=findViewById(R.id.progressBar)
        progress.visibility=View.VISIBLE
        loadData()
    }
}