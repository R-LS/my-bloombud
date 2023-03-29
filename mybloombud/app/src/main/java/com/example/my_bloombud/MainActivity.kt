package com.example.my_bloombud

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.card_recycleview)

        var flower_name_list = ArrayList<String>()
        var img_list = ArrayList<String>()

        recyclerview.adapter =
            HomePageRecyclerViewAdapter(
                applicationContext,
                img_list,
                flower_name_list,
            )

        GlobalScope.launch {
            val allPlants = getAllPlants()
            val plantsArray = JSONArray(allPlants)
            Log.d("B", plantsArray.length().toString())
            for (i in 0 until plantsArray.length()) {
                val plant = plantsArray.getJSONObject(i)
                Log.d("A",plant.get("name").toString())
                flower_name_list.add(plant.get("name").toString())
                img_list.add(plant.get("imageUrl").toString())
                //println("${plant.get("name")} + ${plant.get("imageUrl")}")
            withContext(Dispatchers.Main) {
                //val imageView: ImageView = findViewById(R.id.plantImage)
                //imageView.setImageBitmap(bitmap)
                recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                recyclerview.adapter =
                    HomePageRecyclerViewAdapter(
                        applicationContext,
                        img_list,
                        flower_name_list
                    )

            }
            }
        }

        //Add Intent here -- Scan Plant
        val btn_scanplant = findViewById<Button>(R.id.btn_scanplant)

        btn_scanplant.setOnClickListener {
            //val intent = Intent(this, MyActivity::class.java)
            //startActivity(intent)
        }

        //Add Intent here -- My Plant
        val btn_myplant = findViewById<Button>(R.id.btn_myplant)

        btn_myplant.setOnClickListener {
            //val intent = Intent(this, MyActivity::class.java)
            //startActivity(intent)
        }


        }

    }

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
                    .use { it.readText() }  // defaults to UTF-8
                return_respone = response
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
        return return_respone
    }
