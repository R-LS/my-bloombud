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
import org.json.JSONObject
import java.io.File
import java.util.*

class MyBloomBudsActivity : AppCompatActivity() {
    private var plantList = ArrayList<String>()
    private var plantName = ""
    private var plantSite = ""
    private var plantSpecies = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bloom_buds)
                if(isFilePresent("bloombuds.txt")){
            val scanner1 = Scanner(openFileInput("bloombuds.txt"))
            readFile(scanner1)

        }
//        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,plantList)
//        val plantLv = findViewById<ListView>(R.id.plantListView)
//        plantLv.adapter = myAdapter
        val plantRV = findViewById<RecyclerView>(R.id.plantListRV)
        val courseModelArrayList: ArrayList<PlantModel> = ArrayList<PlantModel>()
        for (plant in plantList){
            if(isFilePresent("$plant.json")){
                readJSON(plant)
                Log.d("readJson,","$plantName,$plantSpecies,$plantSite")
                courseModelArrayList.add(PlantModel(plantName, plantSpecies, plantSite))
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
        plantList = ArrayList<String>()
        if(isFilePresent("bloombuds.txt")){
            val scanner1 = Scanner(openFileInput("bloombuds.txt"))
            readFile(scanner1)
        }
        val plantRV = findViewById<RecyclerView>(R.id.plantListRV)
        val courseModelArrayList: ArrayList<PlantModel> = ArrayList<PlantModel>()
        for (plant in plantList){
            if(isFilePresent("$plant.json")){
                readJSON(plant)
                Log.d("readJson,","$plantName,$plantSpecies,$plantSite")
                courseModelArrayList.add(PlantModel(plantName, plantSpecies, plantSite))
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

        // Access the values in the JSONObject
        plantName = jsonObject.getString("plantName")
        plantSpecies = jsonObject.getString("plantSpecies")
        plantSite = jsonObject.getString("plantSite")
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