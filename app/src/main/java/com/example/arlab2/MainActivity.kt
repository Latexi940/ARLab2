package com.example.arlab2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

private lateinit var fragment: ArFragment
private var testRenderable: ViewRenderable? = null
private var testRenderable2: ViewRenderable? = null
private var testRenderable3: ViewRenderable? = null
private var fitToScanImageView: ImageView? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment
        fitToScanImageView = findViewById(R.id.fit_to_scan_img)

        val renderableFuture = ViewRenderable.builder()
            .setView(this, R.layout.rendtext)
            .build()
        renderableFuture.thenAccept { it -> testRenderable = it }

        val renderableFuture2 = ViewRenderable.builder()
            .setView(this, R.layout.rendtext2)
            .build()
        renderableFuture2.thenAccept { it -> testRenderable2 = it }

        val renderableFuture3 = ViewRenderable.builder()
            .setView(this, R.layout.rendtext3)
            .build()
        renderableFuture3.thenAccept { it -> testRenderable3 = it }

        fragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            frameUpdate()
        }
    }

    private fun frameUpdate() {
        val arFrame = fragment.arSceneView.arFrame

        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {
                TrackingState.PAUSED -> {
                    val text = "Detected image: " + it.name + " - need more info"
                    Log.i("IMGLAB", text)
                }
                TrackingState.STOPPED -> {
                    val text = "Tracking stopped: " + it.name
                    Log.i("IMGLAB", text)
                }
                TrackingState.TRACKING -> {
                    var anchors = it.anchors
                    if (anchors.isEmpty()) {
                        fitToScanImageView?.visibility = View.GONE
                        val pose = it.centerPose
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(fragment.arSceneView.scene)
                        val imgNode = TransformableNode(fragment.transformationSystem)
                        imgNode.setParent(anchorNode)
                        if (it.name == "Uhmaikä") {
                            imgNode.renderable = testRenderable
                            imgNode.setOnTapListener { hitTestRes: HitTestResult?, motionEv: MotionEvent? ->
                                goToUrl("http://www.perunamaa.net/taskarit/taskari.php?numero=283")
                            }
                        }
                        if (it.name == "Harmaan talouden harmit") {
                            imgNode.renderable = testRenderable2
                            imgNode.setOnTapListener { hitTestRes: HitTestResult?, motionEv: MotionEvent? ->
                                goToUrl("http://www.perunamaa.net/taskarit/taskari.php?numero=277")
                            }
                        }
                        if (it.name == "Pahanilmanlinnut") {
                            imgNode.renderable = testRenderable3
                            imgNode.setOnTapListener { hitTestRes: HitTestResult?, motionEv: MotionEvent? ->
                                goToUrl("http://www.perunamaa.net/taskarit/taskari.php?numero=291")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }
}