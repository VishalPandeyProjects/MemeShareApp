package com.internshala.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

      var currentImageUrl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE   //whenever load Meme called show progress bar,
                                                //but the problem is with glide, so we have to add listeners in glide

        val url = "https://meme-api.herokuapp.com/gimme"
         //val memeImageView = findViewById<ImageView>(R.id.memeImageView)
          // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
              currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                                             //this function is when image failed to load,then also we have to stop the visibility
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
//this function when we get the image in glide
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something wrong",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
       MySingleton.getInstance(this ).addToRequestQueue(jsonObjectRequest)   //by this volley will have only one instance till app lifecycle
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)    //these are various action,you can perform with help of intent,ike call ,camera from mobile
          intent.type = "text/plain"   //this will show what type of message app you want, like for sending message it will show messaging app, if video it will show apps to sharre  video
        intent.putExtra(android.content.Intent.EXTRA_TEXT,"Hey check this meme $currentImageUrl")   //this message would be sent when you will send
        //now comes chooser, whcich application we want to send through.

        val chooser = Intent.createChooser(intent,"Select the app...")   //this is to show how to create chooser, and we are passing intent we created here and a message
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}