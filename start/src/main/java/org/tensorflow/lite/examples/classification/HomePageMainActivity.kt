package org.tensorflow.lite.examples.classification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class HomePageMainActivity : AppCompatActivity(), HomePageRecyclerViewInterface{
    val homePagePlantModel_List = ArrayList<HomePagePlantModel>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity_main)

        val recyclerViewInterface1 = this

        //Search bar initialization
        val searchView = findViewById<SearchView>(R.id.searchView)
        //Clear default focus(keyboard) on creation of the application
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //do nothing
                return false
            }
            // if the search text changed, return the filteredList
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText);
                return true
            }
        })
        //Dynamic list initialization
        val recyclerview = findViewById<RecyclerView>(R.id.card_recycleview)
        recyclerview.adapter = HomePageRecyclerViewAdapter(
            this,
            applicationContext,
            homePagePlantModel_List
        )

        //Do HTTP get request
        GlobalScope.launch {
            val allPlants = getAllPlants()
            val plantsArray = JSONArray(allPlants)

            //loop every element return in the json list and put them in model(list)
            for (i in 0 until plantsArray.length()) {
                val plant = plantsArray.getJSONObject(i)
                val model = HomePagePlantModel(plant.get("name").toString(),plant.get("imageUrl").toString(),plant.get("description").toString(),plant.get("supportingText").toString())
                homePagePlantModel_List.add(model)
                withContext(Dispatchers.Main) {
                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    recyclerview.adapter =
                        HomePageRecyclerViewAdapter(
                            recyclerViewInterface1,
                            applicationContext,
                            homePagePlantModel_List
                        )

                }
            }

        }

        val btn_scanplant = findViewById<Button>(R.id.btn_scanplant)
        //Intent to scanplant activity
        btn_scanplant.setOnClickListener {
            val intent = Intent(this, IdentifierActivity::class.java)
            startActivity(intent)
        }


        val btn_myplant = findViewById<Button>(R.id.btn_myplant)
        //Intent to myplant activity
        btn_myplant.setOnClickListener {
            val intent = Intent(this, MyBloomBudsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterList(text: String?) {
        val filteredList: ArrayList<HomePagePlantModel> = ArrayList()
        //text used to compare and search in the existing plant list
        var compare_text = ""
        //if text is null nothing will happened(return to default state)
        if (text == null){

        }else {
            compare_text = text

            //if the current list contains plant name contains search text, add them to filteredList
            for (model in homePagePlantModel_List) {
                if (model.plant_name.lowercase().contains(compare_text.lowercase())) {
                    filteredList.add(model)
                }
            }
            //if filteredList is empty, print error message toastbox
            if (filteredList.isEmpty()){
                if(compare_text!="") {
                    Toast.makeText(this, "No plant found", Toast.LENGTH_SHORT).show()
                }
            }else{
                val recyclerview = findViewById<RecyclerView>(R.id.card_recycleview)
                recyclerview.adapter = HomePageRecyclerViewAdapter(
                    this,
                    applicationContext,
                    filteredList
                )
            }
        }
    }


    override fun onItemClick(position: Int,name : String) {

        //Intent to plant care activity with plant name clicked/selected
        val intent = Intent(this, PlantCare::class.java)
        intent.putExtra("plantName",name)
        startActivity(intent)

    }

}
//HTTP get request to get all plants from api
suspend fun getAllPlants(): String {
    var return_respone = ""
    withContext(Dispatchers.IO) {
        val url = URL("http://my-bloombud-backend-dev-env-env.eba-ui2ndxti.ap-southeast-2.elasticbeanstalk.com/plant")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        // Check if the connection is successful
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // de faults to UTF-8
            return_respone = response
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }
    return return_respone
}
