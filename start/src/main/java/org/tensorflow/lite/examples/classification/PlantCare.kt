package org.tensorflow.lite.examples.classification

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

var plantInfo = ArrayList<String>()

class PlantCare : AppCompatActivity() {

    var plantName = ""
    var plantFile = ""

    private lateinit var loadingBar: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care)
        val plantImageIV = findViewById<ImageView>(R.id.plantImageIV)
        val plantNameTV = findViewById<TextView>(R.id.plantNameTV)
        val plantDescTV = findViewById<TextView>(R.id.plantDescTV)
        val temperatureInfo = findViewById<TextView>(R.id.temperatureInfo)
        val humidityInfo = findViewById<TextView>(R.id.humidityInfo)
        val lightInfo = findViewById<TextView>(R.id.lightInfo)
        val waterInfo = findViewById<TextView>(R.id.waterInfo)
        loadingBar = findViewById<CardView>(R.id.loading_bar)


        // get from prev activity intent
        val it = intent
        plantName = it.getStringExtra("plantName").toString()
        plantNameTV.text = plantName

        GlobalScope.launch {
            showLoadingScreen()
            val imageInfo = getImage(plantName)
            val bitmap =
                BitmapFactory.decodeStream(URL(imageInfo[0]).openConnection().getInputStream())
           plantInfo = getPlantInfo(imageInfo[1])
            withContext(Dispatchers.Main) {
                plantImageIV.setImageBitmap(bitmap)
                plantDescTV.text = plantInfo[0]
                lightInfo.text = plantInfo[1]
                val temperatureString = plantInfo[2] + "°C to " + plantInfo[3] + "°C"
                temperatureInfo.text = temperatureString
                val humidityString = plantInfo[4] + ". " + "Mist every " + plantInfo[5] + " days."
                humidityInfo.text = humidityString
                val waterString = plantInfo[7] + ". " + "Water every " + plantInfo[6] + " days."
                waterInfo.text = waterString
                plantFile = plantInfo[8]
                hideLoadingScreen()
            }
        }
    }

    fun onClickReadingsButton(view: View) {
        val myIntent = Intent(this, PlantRecommendation::class.java)
        myIntent.putExtra("plantName", plantName)
        startActivity(myIntent)
    }

    fun onClickVisualiseButton(view: View) {
        val MIN_OPENGL_VERSION = 3.0
        val openGlVersionString = (applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).deviceConfigurationInfo.glEsVersion

        Log.e("AR test", openGlVersionString)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e("AR test", "Sceneform requires Android N or later")
            Toast.makeText(applicationContext, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
        } else if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e("AR", "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(applicationContext, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show()
        } else {
            val myIntent = Intent(this, PlantVisualiser::class.java)
            myIntent.putExtra("plantFile", plantFile)
            startActivity(myIntent)
        }
    }

    private fun showLoadingScreen() {
        loadingBar.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        loadingBar.visibility = View.GONE
    }
}
