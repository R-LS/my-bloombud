package org.tensorflow.lite.examples.classification

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

var recommendationInfo = ArrayList<String>()
var readings = ArrayList<String>()

class PlantRecommendation : AppCompatActivity() {

    private lateinit var loadingBar: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_recommendation)
        val plantImageIV = findViewById<ImageView>(R.id.plantImageIV)
        val plantNameTV = findViewById<TextView>(R.id.plantNameTV)
        val plantDescTV = findViewById<TextView>(R.id.plantDescTV)
        val temperatureValue = findViewById<TextView>(R.id.temperatureValue)
        val humidityValue = findViewById<TextView>(R.id.humidityValue)
        val lightValue = findViewById<TextView>(R.id.lightValue)
        val temperatureRecommendation = findViewById<TextView>(R.id.temperatureRecommendation)
        val humidityRecommendation = findViewById<TextView>(R.id.humidityRecommendation)
        val lightRecommendation = findViewById<TextView>(R.id.lightRecommendation)
        loadingBar = findViewById<CardView>(R.id.loading_bar)

        // get from prev activity intent
        //val plantName = "Rose"
        val it = intent
        val plantName = it.getStringExtra("plantName").toString()
        plantNameTV.text = plantName


        GlobalScope.launch {
            showLoadingScreen()
            val imageInfo = getImage(plantName)
            val bitmap = BitmapFactory.decodeStream(URL(imageInfo[0]).openConnection().getInputStream())
            readings = getReadings()
            recommendationInfo = getRecommendationInfo(plantName, readings[0], readings[1], readings[2])
            withContext(Dispatchers.Main) {
                plantImageIV.setImageBitmap(bitmap)
                plantDescTV.text = recommendationInfo[0]
                temperatureRecommendation.text = recommendationInfo[2]
                humidityRecommendation.text = recommendationInfo[3]
                lightRecommendation.text = recommendationInfo[1]
                temperatureValue.text = readings[2]
                humidityValue.text = readings[1]
                lightValue.text = readings[0]
                hideLoadingScreen()
            }
        }


    }

    private fun showLoadingScreen() {
        loadingBar.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        loadingBar.visibility = View.GONE
    }
}


