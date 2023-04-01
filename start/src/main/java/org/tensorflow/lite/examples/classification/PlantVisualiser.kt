package org.tensorflow.lite.examples.classification

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.io.IOException
import java.util.function.Consumer
import java.util.function.Function



var flowerRenderable: ModelRenderable? = null
var plantFile = ""


class PlantVisualiser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_visualiser)
        var arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        val it = intent
        plantFile = it.getStringExtra("plantFile").toString()
        Log.e("ar_test", plantFile)
        //val plantName = "Rose"

        /*when (plantName) {
            "Rose" -> plantFile = "file:///android_asset/Red_rose_SF.sfb"
            "Dandelion" -> plantFile = "file:///android_asset/Dandelion.sfb"
            "Sunflower" -> plantFile = "file:///android_asset/sunflower.sfb"
            "Tulip" -> plantFile = "file:///android_asset/tulip.sfb"
            "Daisy" -> plantFile = "file:///android_asset/daisy.sfb"
        }*/

        if (arFragment != null) {
            arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                //Log.e("ar_test3", flowerRenderable.toString())
                placeObject(arFragment, hitResult.createAnchor())

            }
        }


    }

    private fun placeObject(arFragment: ArFragment, anchor: Anchor) {
        //Log.e("ar_test1", flowerRenderable.toString())
        //val assetManager = applicationContext.assets
        //val assetFile = assetManager.open("Red_rose_SF.sfb")
        ModelRenderable.builder()
            .setSource(this, Uri.parse(plantFile))
            .build()
            .thenAccept { renderable ->
                Log.e("ar_test2", flowerRenderable.toString())
                flowerRenderable = renderable
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment.arSceneView.scene)

                // Attach the renderable to the anchor node
                val renderableNode = TransformableNode(arFragment.transformationSystem)
                renderableNode.setParent(anchorNode)
                renderableNode.renderable = flowerRenderable
                renderableNode.select()
            }
            .exceptionally { throwable ->
                Log.e("ar_test4", flowerRenderable.toString())
                val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }
    }

}

