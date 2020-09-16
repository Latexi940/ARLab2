package com.example.arlab2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class AImgFrag : ArFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = super.getSessionConfiguration(session)
        setupAugmentedImageDatabase(config, session)
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session?) {
        val assetManager = context!!.assets

        val inputStream1 = assetManager.open("aku.jpg")
        val augmentedImageBitmap1 = BitmapFactory.decodeStream(inputStream1)

        val inputStream2 = assetManager.open("aku2.jpg")
        val augmentedImageBitmap2 = BitmapFactory.decodeStream(inputStream2)

        val inputStream3 = assetManager.open("aku3.jpg")
        val augmentedImageBitmap3 = BitmapFactory.decodeStream(inputStream3)

        val augmentedImageDb = AugmentedImageDatabase(session)
        augmentedImageDb.addImage("Uhmaikä", augmentedImageBitmap1)
        augmentedImageDb.addImage("Harmaan talouden harmit", augmentedImageBitmap2)
        augmentedImageDb.addImage("Pahanilmanlinnut", augmentedImageBitmap3)

        config.augmentedImageDatabase = augmentedImageDb
    }
}