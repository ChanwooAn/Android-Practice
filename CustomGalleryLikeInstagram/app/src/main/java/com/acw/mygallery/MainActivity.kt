package com.acw.mygallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import run.acw.crew.util.imagepicker.PhotoPickerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button=findViewById<Button>(R.id.btn1)
        button.setOnClickListener {
            requestPermission.launch(permissionList)
        }
    }
    private val permissionList = Manifest.permission.READ_MEDIA_IMAGES
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Toast.makeText(this, "파일 권한 허가", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, PhotoPickerActivity::class.java))
            }
            false -> {
                Toast.makeText(this, "파일 권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
    }
}