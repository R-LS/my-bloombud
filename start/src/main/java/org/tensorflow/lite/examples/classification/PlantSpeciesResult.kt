package org.tensorflow.lite.examples.classification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    }

}