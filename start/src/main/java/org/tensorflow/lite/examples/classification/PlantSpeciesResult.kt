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
import java.io.File

class PlantSpeciesResult : AppCompatActivity() {
    var imagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_species_result)

        //retrieve results from previous screen to here
        val it = intent

        val plant1 = findViewById<TextView>(R.id.plant1)
        val plant2 = findViewById<TextView>(R.id.plant2)
        val plant3 = findViewById<TextView>(R.id.plant3)

        //Log.d("recognition extra",it.getStringExtra("species").toString())

        plant1.text = it.getStringExtra("species1")
        plant2.text = it.getStringExtra("species2")
        plant3.text = it.getStringExtra("species3")

        // Retrieve the file path from the intent
        imagePath = intent.getStringExtra("image_path")!!

        // Load the image from the file path into an ImageView
        val imageView = findViewById<ImageView>(R.id.plantImageView)
        Log.d("file image path",imagePath)
        imageView.setImageURI(Uri.parse(imagePath))

    }

    fun cleanup(){
        //remove temporary image file
        val imageFile = File(imagePath)

        if (imageFile.exists()) {
            imageFile.delete()
            println("Image deleted.")
        } else {
            println("Image file not found.")
        }

    }

    fun onClickBloomBuds(v: View){
        //navigate to mybloombuds screen
        val myIntent = Intent(this, MyBloomBudsActivity::class.java)
        cleanup()
        startActivity(myIntent)

    }
    fun onClickReturn(v: View){
        //return to previous screen (camera identifier screen)
        cleanup()
        finish()
    }

    fun backToHome(v: View){
        //navigate to homepage screen
        val myIntent = Intent(this, HomePageMainActivity::class.java)
        cleanup()
        startActivity(myIntent)
    }

}