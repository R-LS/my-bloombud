package org.tensorflow.lite.examples.classification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.File
import java.io.PrintStream

class EditBloomBudActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bloom_bud)
        val it = intent
        val plant = it.getStringExtra("plantName").toString()
        readJSON(plant)

    }
    private fun readJSON(fileName:String){
        // Load the JSON file as a string
        val path = filesDir.toString() + "/" + fileName+".json"
        val jsonString = File(path).readText(Charsets.UTF_8)

        // Parse the JSON string into a JSONObject
        val jsonObject = JSONObject(jsonString)

        // Access the values in the JSONObject
        val plantName = jsonObject.getString("plantName")
        val plantSpecies = jsonObject.getString("plantSpecies")
        val plantSite = jsonObject.getString("plantSite")

        findViewById<EditText>(R.id.plantName).setText(plantName)
        findViewById<EditText>(R.id.plantSpecies).setText(plantSpecies)
        findViewById<EditText>(R.id.plantSite).setText(plantSite)
    }
    fun onClickEdit(v: View){
        val plantName = findViewById<TextInputEditText>(R.id.plantName).text.toString()
        val plantSite = findViewById<TextInputEditText>(R.id.plantSite).text.toString()
        val plantSpecies = findViewById<TextInputEditText>(R.id.plantSpecies).text.toString()
        modifyJSON(plantName,plantSite,plantSpecies)
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
        Toast.makeText(this,"Successfully edited $plantName the $plantSpecies!", Toast.LENGTH_SHORT).show()
    }
    private fun modifyJSON(plantName:String, plantSite:String, plantSpecies:String){
        // Create a JSON object
        val jsonObject = JSONObject()
        jsonObject.put("plantName", plantName)
        jsonObject.put("plantSite", plantSite)
        jsonObject.put("plantSpecies",plantSpecies)

        // Write the JSON object to a file
        val jsonString = jsonObject.toString()
        val outStream = PrintStream(openFileOutput("$plantName.json", MODE_APPEND))

        outStream.print(jsonString)
        outStream.close()

    }

}