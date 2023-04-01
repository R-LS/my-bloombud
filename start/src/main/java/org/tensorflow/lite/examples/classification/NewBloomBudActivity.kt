package org.tensorflow.lite.examples.classification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

private const val TEMP_FILE_NAME = "temp_plant_photo.jpg"
private const val TAKE_PHOTO_REQUEST_CODE = 42
private const val SELECT_PHOTO_REQUEST_CODE = 43
class NewBloomBudActivity : AppCompatActivity() {
    private var tempPhotoFile: File? = null
    lateinit var plantImageView: ImageView
    private lateinit var selectedPlantSpecies: String
    private var plantList = ArrayList<String>()
    val myPlantSpeciesList: ArrayList<String> = arrayListOf("Rose","Daisy","Dandelion","Sunflower","Tulip")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_bloom_bud)

        plantImageView = findViewById<ImageView>(R.id.plantImageView)

        val btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener { takePhoto() }

        val btnSelectPhoto = findViewById<Button>(R.id.btnSelectPhoto)
        btnSelectPhoto.setOnClickListener { selectPhoto() }
        if (isFilePresent("bloombuds.json")){
            loadPlantList()
        }

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

    }
    //Add photo functions

    fun takePhoto() {
        val hasCameraPermissions = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasCameraPermissions) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                TAKE_PHOTO_REQUEST_CODE
            )
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        tempPhotoFile = createTempPhotoFileForStorage(TEMP_FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            tempPhotoFile!!
        )

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        if (takePictureIntent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            return
        }
        startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE)
    }

    private fun createTempPhotoFileForStorage(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(tempPhotoFile!!.absolutePath)
            plantImageView.setImageBitmap(takenImage)
        } else if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data!!
            plantImageView.setImageURI(selectedImageUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val hasAllowedPermissions =
            grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED
        if (!hasAllowedPermissions) {
            Toast.makeText(this, "Please allow the required permissions", Toast.LENGTH_SHORT).show()
            return
        }

        when (requestCode) {
            TAKE_PHOTO_REQUEST_CODE -> {
                takePhoto()
            }
            SELECT_PHOTO_REQUEST_CODE -> {
                selectPhoto()
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun selectPhoto() {
        val hasGalleryPermissions = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasGalleryPermissions) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                SELECT_PHOTO_REQUEST_CODE
            )
            return
        }

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        )

        startActivityForResult(galleryIntent, SELECT_PHOTO_REQUEST_CODE)
    }


    private fun savePlantImage(plantPetName: String) {
        try {
            var bitmapDrawable = plantImageView.drawable
            var bitmap = bitmapDrawable.toBitmap()

            val fileName = "${plantPetName}.jpg"
            val outputStream = openFileOutput(fileName, MODE_PRIVATE)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            //Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong while saving image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // other plant detail functions


    fun onClickCreateNew(v: View){
        val plantName = findViewById<TextInputEditText>(R.id.plantName).text.toString()
        val plantSite = findViewById<TextInputEditText>(R.id.plantSite).text.toString()
        //val plantSpecies = findViewById<TextInputEditText>(R.id.plantSpecies).text.toString()
        if(plantList.contains(plantName)){
            Toast.makeText(this,"A plant by this name already exists!", Toast.LENGTH_SHORT).show()
        }
        else if(plantName.isNullOrBlank()){
            Toast.makeText(this,"Plant Name is required!", Toast.LENGTH_SHORT).show()
        }
        else{
            modifyJSON(plantName,plantSite,selectedPlantSpecies)
            savePlantImage(plantName)
            //editPlantList(plantName)
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
            Toast.makeText(this,"Successfully added $plantName the $selectedPlantSpecies!", Toast.LENGTH_SHORT).show()

        }

    }

    private fun loadPlantList(){
        val fileName = filesDir.toString() + "/bloombuds.json"
        var file = File(fileName)

            val json = file.bufferedReader().use{it.readText()}
            var jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                //Log.d("eachJson,",jsonObject.toString())
                plantList.add(jsonObject.getString("plantName"))
            }

    }

    private fun isFilePresent(fileName: String): Boolean {
        val path = filesDir.toString() + "/" + fileName
        val file = File(path)
        Log.d("filething", path)
        Log.d("filething", file.exists().toString())
        return file.exists()
    }



    private fun modifyJSON(plantName:String, plantSite:String, plantSpecies:String){
        //var jsonObject = JSONObject()
        var jsonArray = JSONArray()
        val fileName = filesDir.toString() + "/bloombuds.json"

        var file = File(fileName)

        val isNewFileCreated :Boolean = file.createNewFile()
        if(!isNewFileCreated){
        val json = file.bufferedReader().use{it.readText()}
        jsonArray = JSONArray(json)}

        val newPlantObject = JSONObject()
        newPlantObject.put("plantName", plantName)
        newPlantObject.put("plantSite", plantSite)
        newPlantObject.put("plantSpecies",plantSpecies)
        //jsonObject.put(plantName,newPlantObject)
        jsonArray.put(newPlantObject)
        val outputStream = FileOutputStream(fileName)
        val printStream = PrintStream(outputStream)
        printStream.println(jsonArray) // write data to the file, which will overwrite the existing file contents
        printStream.close()

    }


}
