package org.tensorflow.lite.examples.classification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*

class MyBloomBudsActivity : AppCompatActivity() {
    private var plantList = ArrayList<String>()
    private var courseModelArrayList: ArrayList<PlantModel> = ArrayList<PlantModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bloom_buds)
        val plantRV = findViewById<RecyclerView>(R.id.plantListRV)
        Log.d("bloombud","hit onCreate ")

        Log.d("bloombud isFilePresent",isFilePresent("bloombuds.json").toString())
            if(isFilePresent("bloombuds.json")){
                //readJSON(plant)
                val path = filesDir.toString() + "/bloombuds.json"
                //val jsonString = File(path).readText(Charsets.UTF_8)
                var file = File(path)

                // Parse the JSON string into a JSONObject
                val json = file.bufferedReader().use{it.readText()}
                val jsonArray = JSONArray(json)
                Log.d("readJson,",jsonArray.toString())

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    Log.d("eachJson,",jsonObject.toString())
                    courseModelArrayList.add(PlantModel(jsonObject.getString("plantName"), jsonObject.getString("plantSpecies"), jsonObject.getString("plantSite")))
                }

            }

        val courseAdapter = PlantListAdapter(this, courseModelArrayList)
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        plantRV.layoutManager = linearLayoutManager
        plantRV.adapter = courseAdapter

    }

    override fun onResume() {
        super.onResume()
        courseModelArrayList = ArrayList<PlantModel>()
        val plantRV = findViewById<RecyclerView>(R.id.plantListRV)
        Log.d("bloombud","hit onCreate ")

        Log.d("bloombud isFilePresent",isFilePresent("bloombuds.json").toString())
        if(isFilePresent("bloombuds.json")){
            //readJSON(plant)
            val path = filesDir.toString() + "/bloombuds.json"
            //val jsonString = File(path).readText(Charsets.UTF_8)
            var file = File(path)


            // Parse the JSON string into a JSONObject
            val json = file.bufferedReader().use{it.readText()}
            val jsonArray = JSONArray(json)
            Log.d("readJson,",jsonArray.toString())


            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                Log.d("eachJson,",jsonObject.toString())
                courseModelArrayList.add(PlantModel(jsonObject.getString("plantName"), jsonObject.getString("plantSpecies"), jsonObject.getString("plantSite")))
            }

        }


        val courseAdapter = PlantListAdapter(this, courseModelArrayList)
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        plantRV.layoutManager = linearLayoutManager
        plantRV.adapter = courseAdapter

    }
        private fun readFile(scanner: Scanner){
        while(scanner.hasNextLine()){
            val line = scanner.nextLine()
            val pieces = line.split("\t")
            plantList.add(pieces[0])
        }
    }
    private fun readJSON(fileName:String){
        // Load the JSON file as a string
        val path = filesDir.toString() + "/" + fileName+".json"
        val jsonString = File(path).readText(Charsets.UTF_8)

        // Parse the JSON string into a JSONObject
        val jsonObject = JSONObject(jsonString)

        for (k in jsonObject.keys()){
            var plantRecord = jsonObject.getJSONObject(k)
            var plantName = plantRecord.getString("plantName")
            var plantSpecies = plantRecord.getString("plantSpecies")
            var plantSite = plantRecord.getString("plantSite")
            //courseModelArrayList.add(PlantModel(plantName, plantSpecies, plantSite))

        }

        // Access the values in the JSONObject
//        plantName = jsonObject.getString("plantName")
//        plantSpecies = jsonObject.getString("plantSpecies")
//        plantSite = jsonObject.getString("plantSite")
    }

    private fun isFilePresent(fileName: String): Boolean {
        val path = filesDir.toString() + "/" + fileName
        val file = File(path)
        Log.d("filething", path)
        Log.d("filething", file.exists().toString())
        return file.exists()
    }
    fun onAddNewPlant(v: View){
        val myIntent = Intent(this, NewBloomBudActivity::class.java)
        startActivity(myIntent)
    }

}