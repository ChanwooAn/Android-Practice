package com.acw.mygallery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import run.acw.crew.util.imagepicker.PhotoPickerActivity

class MainActivity : AppCompatActivity() {
    private val cropActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val croppedImage = data?.getStringExtra(CROPPED_IMAGES)
            // Crop된 이미지를 사용하고자 하는 곳에서 처리
            // 예: ImageView에 설정하거나, 파일로 저장하거나, 네트워크로 전송하거나 등등.
            Log.d("registerCallback",croppedImage.toString())
            setImage(croppedImage!!)
        }
    }

    private fun startCropActivity() {
        val intent = Intent(this, PhotoPickerActivity::class.java)
        cropActivityResultLauncher.launch(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button=findViewById<Button>(R.id.btn1)
        button.setOnClickListener {
            requestPermission.launch(permissionList)
        }



    }
    private fun setImage(img:String){
        val imgView=findViewById<ImageView>(R.id.crop_image_view_main)
        Glide
            .with(this)
            .load(img)
            .into(imgView)

    }

    private val permissionList = Manifest.permission.READ_MEDIA_IMAGES
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Toast.makeText(this, "파일 권한 허가", Toast.LENGTH_SHORT).show()
                startCropActivity()
            }
            false -> {
                Toast.makeText(this, "파일 권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CROP = 1
        const val CROPPED_IMAGES="cropped images"
    }
}