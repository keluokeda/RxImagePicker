package com.ke.rximagepicker.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.ke.rximagepicker.RxImagePicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.imageView)

        findViewById<View>(R.id.camera).setOnClickListener {
            RxImagePicker(this)
                .pick(RxImagePicker.SOURCE_CAMERA)
                .subscribe {
                    imageView.setImageURI(it.uri)
                }
        }

        findViewById<View>(R.id.gallery).setOnClickListener {
            RxImagePicker(this)
                .pick(RxImagePicker.SOURCE_GALLERY)
                .subscribe {
                    imageView.setImageURI(it.uri)
                }
        }
    }
}