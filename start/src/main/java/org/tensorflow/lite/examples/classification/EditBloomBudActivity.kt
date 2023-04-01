package org.tensorflow.lite.examples.classification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

class EditBloomBudActivity : AppCompatActivity() {
    private lateinit var plantName: String
    private lateinit var plantSite: String
    private lateinit var plantSpecies: String
    private var plantID = 0
    private lateinit var selectedPlantSpecies: String
    val myPlantSpeciesList: ArrayList<String> = arrayListOf("Rose","Daisy","Dandelion","Sunflower","Tulip")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bloom_bud)
        val it = intent
        plantName = it.getStringExtra("plantName").toString()
        readJSON(plantName)
        findViewById<TextInputEditText>(R.id.plantName).setText(plantName)
        findViewById<TextInputEditText>(R.id.plantSite).setText(plantSite)
        val mySpinner = findViewById<Spinner>(R.id.dropdown)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, myPlantSpeciesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = spinnerAdapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedPlantSpecies = parent.getItemAtPosition(position) as String

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        mySpinner.setSelection(myPlantSpeciesList.indexOf(plantSpecies))

    }
    private fun readJSON(seekPlant:String){
        // Load the JSON file as a string
        val path = filesDir.toString() + "/bloombuds.json"
        var file = File(path)

        // Parse the JSON string into a JSONObject
        val json = file.bufferedReader().use{it.readText()}
        val jsonArray = JSONArray(json)

        //Log.d("readJson,",jsonArray.toString())

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            Log.d("eachJson,",jsonObject.toString())
            if(jsonObject.getString("plantName") == seekPlant){
                plantID = i
                plantSpecies = jsonObject.getString("plantSpecies")
                plantSite = jsonObject.getString("plantSite")
                break
            }
        }

    }
    fun onClickEdit(v: View){
        val newPlantName = findViewById<TextInputEditText>(R.id.plantName).text.toString()
        val newPlantSite = findViewById<TextInputEditText>(R.id.plantSite).text.toString()
        modifyJSON(newPlantName,newPlantSite,selectedPlantSpecies)
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
        Toast.makeText(this,"Successfully edited $newPlantName the $selectedPlantSpecies!", Toast.LENGTH_SHORT).show()
    }
    private fun modifyJSON(newPlantName:String, newPlantSite:String, newPlantSpecies:String){
        // Create a JSON object
        val jsonObject = JSONObject()
        jsonObject.put("plantName", newPlantName)
        jsonObject.put("plantSite", newPlantSite)
        jsonObject.put("plantSpecies",newPlantSpecies)

        //Modify the json file
        val path = filesDir.toString() + "/" + "bloombuds.json"
        var file = File(path)

        // Parse the JSON string into a JSONObject
        val json = file.bufferedReader().use{it.readText()}
        val jsonArray = JSONArray(json)

        jsonArray.put(plantID,jsonObject)
        val outputStream = FileOutputStream(path)
        val printStream = PrintStream(outputStream)
        printStream.println(jsonArray) // write data to the file, which will overwrite the existing file contents
        printStream.close()
        val oldFile = File(filesDir.toString()+"/$plantName.jpg")
        val newFile = File(filesDir.toString()+"/$newPlantName.jpg")

        if (oldFile.exists()) {
            val success = oldFile.renameTo(newFile)
            if (success) {
                // the file was renamed successfully
            } else {
                // an error occurred while renaming the file
            }
        } else {
            // the file does not exist
        }

    }

}