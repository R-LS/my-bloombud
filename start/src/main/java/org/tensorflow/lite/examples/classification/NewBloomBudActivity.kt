package org.tensorflow.lite.examples.classification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

class NewBloomBudActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_bloom_bud)

    }


    fun onClickCreateNew(v: View){
        val plantName = findViewById<TextInputEditText>(R.id.plantName).text.toString()
        val plantSite = findViewById<TextInputEditText>(R.id.plantSite).text.toString()
        val plantSpecies = findViewById<TextInputEditText>(R.id.plantSpecies).text.toString()
        createJSON(plantName,plantSite,plantSpecies)
        editPlantList(plantName)
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
        Toast.makeText(this,"Successfully added $plantName the $plantSpecies!", Toast.LENGTH_SHORT).show()



    }
     private fun editPlantList(newPlant:String){
         val outStream = PrintStream(openFileOutput("bloombuds.txt", MODE_APPEND))
         outStream.println(newPlant)
         outStream.close()
     }


    private fun modifyJSON(plantName:String, plantSite:String, plantSpecies:String){
        var jsonObject = JSONObject()
        val fileName = "bloombuds.json"

        var file = File(fileName)

        // create a new file
        val isNewFileCreated :Boolean = file.createNewFile()

        if(isNewFileCreated){
            Log.d("filesuccess","$fileName is created successfully.")
        } else{
            val jsonString = file.readText(Charsets.UTF_8)
            // Parse the JSON string into a JSONObject
            jsonObject = JSONObject(jsonString)
        }

        val newPlantObject = JSONObject()
        //newPlantObject.put("plantName", plantName)
        newPlantObject.put("plantSite", plantSite)
        newPlantObject.put("plantSpecies",plantSpecies)
        jsonObject.put(plantName,newPlantObject)
        val outputStream = FileOutputStream(fileName)
        val printStream = PrintStream(outputStream)
        printStream.println(jsonObject) // write data to the file, which will overwrite the existing file contents
        printStream.close()

    }

    private fun createJSON(plantName:String, plantSite:String, plantSpecies:String){
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
