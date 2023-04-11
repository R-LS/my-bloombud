package com.example.plantvisualiser

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

// initialise variables
var flowerRenderable: ModelRenderable? = null
var plantFile = ""
private lateinit var loadingBar: View


class PlantVisualiser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_visualiser)
        var arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        loadingBar = findViewById<ProgressBar>(R.id.loading_bar)
        hideLoadingScreen()

        val it = intent
        plantFile = it.getStringExtra("plantFile").toString()
        Log.e("ar_test", plantFile)

        if (arFragment != null) {
            arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                placeObject(arFragment, hitResult.createAnchor())

            }
        }


    }

    // load the 3d model from the file and place the object, displaying the 3d model
    private fun placeObject(arFragment: ArFragment, anchor: Anchor) {
        showLoadingScreen()
        ModelRenderable.builder()
            .setSource(this, Uri.parse(plantFile))
            .build()
            .thenAccept { renderable ->
                hideLoadingScreen()
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
                val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }
    }

    private fun showLoadingScreen() {
        loadingBar.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        loadingBar.visibility = View.GONE
    }

}

