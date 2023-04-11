package com.example.mygallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mygallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val imageLoadLauncher=registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        uriList->
            updateImages(uriList)
    }
    private lateinit var imageAdapter:ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener{
            checkPermission()
        }
        initRecyclerView()
    }
    fun initRecyclerView(){
        imageAdapter= ImageAdapter(object:ImageAdapter.ItemClickListener{
            override fun onLoadMoreClick() {
                checkPermission()
            }
        })
        binding.imageRecyclerView.apply {
            adapter=imageAdapter
            layoutManager=GridLayoutManager(context,3)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            READ_EXTERNAL_IMAGE->{
                if(grantResults.firstOrNull()==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }
            }
        }

    }
    private fun checkPermission(){
        Log.d("PermissionFlow","checkPermission")

        when{
           /* shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)->{
                Log.d("PermissionFlow","rational")
                //사용자가 명시적으로 권한을 거부했을 때 true를 반환한다.

                showPermissionInfoDialog()
            }*/

            ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED->{
                //권한을 허용했을 때
                loadImage()
            }

            !shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)->{
                Log.d("PermissionFlow","rational")
                //다시 묻지 않음을 선택하거나 , 권한 요청을 처음 보거나, 권한을 허용한 경우 false를 반환

                openAppSettings()
            }



            else->{
                Log.d("PermissionFlow","else")
                showPermissionInfoDialog()
            }
        }
    }

    private val appSettingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            checkPermission()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        appSettingsLauncher.launch(intent)
    }

    private fun showPermissionInfoDialog(){
        Log.d("PermissionFlow","Info")
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서 권한부여가 필요합니다.")
            setPositiveButton("확인"){ _,_->
                showPermissionRequestDialog()
            }
            setNegativeButton("취소",null)
        }.show()
    }

    private fun showPermissionRequestDialog(){
        Log.d("PermissionFlow","showPermissionRequest")
ss
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), READ_EXTERNAL_IMAGE)
    }
    private fun loadImage(){
        imageLoadLauncher.launch("image/*")
    }
    private fun updateImages(uriList:List<Uri>){
        Log.d("Images",uriList.toString())
        val images=uriList.map { ImageItems.Image(uri=it) }
        imageAdapter.submitList(images)
    }
    companion object{
        const val READ_EXTERNAL_IMAGE=100
    }
}