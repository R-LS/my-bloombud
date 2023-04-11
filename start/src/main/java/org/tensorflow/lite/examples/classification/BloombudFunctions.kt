package org.tensorflow.lite.examples.classification

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

// get the sensor readings from thingspeak
suspend fun getReadings() : ArrayList<String> {
    var sensorReadings = ArrayList<String>()
    withContext(Dispatchers.IO) {
        val url = URL("https://api.thingspeak.com/channels/2077930/feeds/last.json")
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
            withContext(Dispatchers.Main) {
                val jsonObject = JsonParser.parseString(response).asJsonObject
                sensorReadings.add(jsonObject.get("field2").asString)
                sensorReadings.add(jsonObject.get("field3").asString)
                sensorReadings.add(jsonObject.get("field1").asString)
            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }
    return sensorReadings
}
// get the image of a flower from BloomBud API
suspend fun getImage(plantName : String) : ArrayList<String> {
    var imagePlantInfo = ArrayList<String>()
    withContext(Dispatchers.IO) {
        val url = URL("http://my-bloombud-backend-dev-env-env.eba-ui2ndxti.ap-southeast-2.elasticbeanstalk.com/plant?name=" + plantName)
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
            withContext(Dispatchers.Main) {
                val jsonArray = JsonParser.parseString(response).asJsonArray
                for (jsonElement in jsonArray) {
                    val jsonObject = jsonElement.asJsonObject
                    val imageUrl = jsonObject.get("imageUrl").asString
                    imagePlantInfo.add(imageUrl)
                    val plantId = jsonObject.get("plantId").asString
                    imagePlantInfo.add(plantId)
                }
            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }
    return imagePlantInfo
}

// get the recommended information from BloomBud API, based on the current readings
suspend fun getRecommendationInfo(plantName: String, lightCurrent: String, humidityCurrent: String, temperatureCurrent: String): ArrayList<String> {
    var recInfo = ArrayList<String>()
    // Create JSON using JSONObject
    val jsonObject = JSONObject()
    jsonObject.put("name", plantName)
    jsonObject.put("light", lightCurrent)
    jsonObject.put("humidity", humidityCurrent)
    jsonObject.put("temperature", temperatureCurrent)

    // Convert JSONObject to String
    val jsonObjectString = jsonObject.toString()

    withContext(Dispatchers.IO) {
        val url = URL("http://my-bloombud-backend-dev-env-env.eba-ui2ndxti.ap-southeast-2.elasticbeanstalk.com/plant/analysis")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.setRequestProperty("Content-Type", "application/json")
        httpURLConnection.setRequestProperty("Accept", "application/json")
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = true

        // Send the JSON we created
        val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()

        // Check if the connection is successful
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            withContext(Dispatchers.Main) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(JsonParser.parseString(response))
                Log.d("Pretty Printed JSON :", prettyJson)
                // create a JSONObject from the string
                val jsonObj = JSONObject(prettyJson)
                val plantDesc = jsonObj.getJSONObject("plant").getJSONObject("lifecycleInfo").getString("description")
                recInfo.add(plantDesc)
                val plantLightRec = jsonObj.getJSONObject("lightRecommendation").getString("result")
                val plantLightAdv = jsonObj.getJSONObject("lightRecommendation").getString("advice")
                recInfo.add(plantLightRec + "\n" + plantLightAdv)
                val plantTempRec = jsonObj.getJSONObject("temperatureRecommendation").getString("result")
                val plantTempAdv = jsonObj.getJSONObject("temperatureRecommendation").getString("advice")
                recInfo.add(plantTempRec + "\n" + plantTempAdv)
                val plantHumidityRec = jsonObj.getJSONObject("humidityRecommendation").getString("result")
                val plantHumidityAdv = jsonObj.getJSONObject("humidityRecommendation").getString("advice")
                recInfo.add(plantHumidityRec + "\n" + plantHumidityAdv)

            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }

    return recInfo
}

// get the flower information and 3d model file from BloomBud API
suspend fun getPlantInfo(plantId: String): ArrayList<String> {
    var pInfo = ArrayList<String>()

    withContext(Dispatchers.IO) {
        val url = URL("http://my-bloombud-backend-dev-env-env.eba-ui2ndxti.ap-southeast-2.elasticbeanstalk.com/plant/" + plantId)
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
            withContext(Dispatchers.Main) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(JsonParser.parseString(response))
                Log.d("Pretty Printed JSON :", prettyJson)
                // create a JSONObject from the string
                val jsonObj = JSONObject(prettyJson)
                val plantDesc = jsonObj.getJSONObject("lifecycleInfo").getString("description")
                pInfo.add(plantDesc)
                val plantLightDesc = jsonObj.getJSONObject("lightInfo").getString("preferredLight")
                pInfo.add(plantLightDesc)
                val plantTempMin = jsonObj.getJSONObject("temperatureInfo").getString("minValue")
                pInfo.add(plantTempMin)
                val plantTempMax = jsonObj.getJSONObject("temperatureInfo").getString("maxValue")
                pInfo.add(plantTempMax)
                val plantHumidityDesc = jsonObj.getJSONObject("humidityInfo").getString("description")
                pInfo.add(plantHumidityDesc)
                val plantMistingFreq = jsonObj.getJSONObject("humidityInfo").getString("mistingFrequencyInDays")
                pInfo.add(plantMistingFreq)
                val plantWaterFreq = jsonObj.getJSONObject("waterInfo").getString("wateringFrequencyInDays")
                pInfo.add(plantWaterFreq)
                val plantWaterGuide = jsonObj.getJSONObject("waterInfo").getString("wateringGuideline")
                pInfo.add(plantWaterGuide)
                val plantModelURL = jsonObj.getString("arModelUrl")
                pInfo.add(plantModelURL)

            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }

    return pInfo
}