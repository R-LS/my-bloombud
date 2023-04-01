package org.tensorflow.lite.examples.classification

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class PlantSpeciesResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_species_result)

        val it = intent

        val plant1 = findViewById<TextView>(R.id.plant1)
        val plant2 = findViewById<TextView>(R.id.plant2)
        val plant3 = findViewById<TextView>(R.id.plant3)

        Log.d("recognition extra",it.getStringExtra("species").toString())

        plant1.text = it.getStringExtra("species1")
        plant2.text = it.getStringExtra("species2")
        plant3.text = it.getStringExtra("species3")

        // Retrieve the file path from the intent
        val imagePath = intent.getStringExtra("image_path")

        // Load the image from the file path into an ImageView
        val imageView = findViewById<ImageView>(R.id.plantImageView)
        imageView.setImageURI(Uri.parse(imagePath))

    }

    fun onClickBloomBuds(v: View){
        val myIntent = Intent(this, MyBloomBudsActivity::class.java)
        startActivity(myIntent)

    }
    fun onClickReturn(v: View){
        finish()
    }

    fun backToHome(v: View){
        val myIntent = Intent(this, HomePageMainActivity::class.java)
        startActivity(myIntent)
    }

}