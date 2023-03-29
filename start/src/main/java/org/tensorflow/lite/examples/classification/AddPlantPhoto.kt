package org.tensorflow.lite.examples.classification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import java.io.File

private const val TEMP_FILE_NAME = "temp_plant_photo.jpg"
private const val TAKE_PHOTO_REQUEST_CODE = 42
private const val SELECT_PHOTO_REQUEST_CODE = 43
class AddPlantPhoto : AppCompatActivity() {
    private var tempPhotoFile: File? = null
    lateinit var plantImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plant_photo)


        plantImageView = findViewById<ImageView>(R.id.plantImageView)

        val btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener { takePhoto() }

        val btnSelectPhoto = findViewById<Button>(R.id.btnSelectPhoto)
        btnSelectPhoto.setOnClickListener { selectPhoto() }

        val btnAddPlant = findViewById<Button>(R.id.btnAddPlant)
        btnAddPlant.setOnClickListener { addPlant() }

    }

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

    private fun addPlant() {
        savePlantImage("testing1245")
    }

    private fun savePlantImage(plantPetName: String) {
        try {
            var bitmapDrawable = plantImageView.drawable
            var bitmap = bitmapDrawable.toBitmap()

            val fileName = "${plantPetName}.jpg"
            val outputStream = openFileOutput(fileName, MODE_PRIVATE)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong while saving image", Toast.LENGTH_SHORT)
                .show()
        }
    }
}