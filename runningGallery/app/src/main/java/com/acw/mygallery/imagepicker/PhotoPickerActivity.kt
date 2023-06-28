package run.acw.crew.util.imagepicker

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acw.mygallery.MainActivity
import com.acw.mygallery.databinding.CustomPhotoPickerBinding
import com.acw.mygallery.databinding.PhotoPickerFolderListBinding
import com.acw.mygallery.imagepicker.ImageFolderDialog
import com.acw.mygallery.imagepicker.ImageFolderDialogListener
import com.acw.mygallery.imagepicker.PhotoPickerViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PhotoPickerActivity : AppCompatActivity(), ImageFolderDialogListener,
    ImageAdapter.OnImageClicked {

    private var _binding: CustomPhotoPickerBinding? = null
    private val binding get() = _binding!!

    private val imgAdapter = ImageAdapter(this)
    private val viewModel: PhotoPickerViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[PhotoPickerViewModel::class.java]
    }

    private val photoListObserver: Observer<List<String>> = Observer { photoList ->
        imgAdapter.setPicturePaths(photoList)
        if (photoList.isNotEmpty()) {
            binding.cropImageView.visibility = View.VISIBLE
            loadImageToCropImageView(photoList[0])
        } else {
            binding.cropImageView.visibility = View.INVISIBLE
        }
    }
    private val folderNameObserver: Observer<String> = Observer { folderName ->
        binding.galleryNameText.text = folderName
    }

    override fun onDialogResult(data: String) {
        //여기서 받은 폴더이름으로 getImage하기.
        viewModel.setFolderName(data)
        viewModel.getPhotoList(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = CustomPhotoPickerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        _binding = viewBinding
        supportActionBar?.hide()

        binding.imageRecycler.apply {
            adapter = imgAdapter
            layoutManager = GridLayoutManager(this@PhotoPickerActivity, 3)
        }
        binding.folderName.setOnClickListener {
            ImageFolderDialog().show(supportFragmentManager, "ImageFolderDialog")
        }
        viewModel.photoList.observe(
            this@PhotoPickerActivity,
            photoListObserver
        )//crop view에 처음 setting하기
        viewModel.folderName.observe(this@PhotoPickerActivity, folderNameObserver)
        viewModel.getPhotoList(this)

        binding.cropButton.setOnClickListener {
            Log.d("qwdqwdqwdqwd", "click")

            val bitmapCropped = binding.cropImageView.cropBitmap()//crop 된 이미지 가지고오기
            val saveCroppedfile=saveImageLocally(bitmapCropped)


            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(MainActivity.CROPPED_IMAGES, saveCroppedfile.absolutePath)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }//일단 잘 사진이 잘 잘리는지 test
    }

    private fun saveImageLocally(bitmap: Bitmap): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val file = File(storageDir, fileName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }
    override fun onStop() {
        super.onStop()
        viewModel.photoList.removeObserver(photoListObserver)
        // observer 내부에서 view작업이 이루어지기 때문에 onStop에서 명시적으로 해제.
    }

    private fun loadImageToCropImageView(imgUrl: String) {
        Glide
            .with(this@PhotoPickerActivity)
            .asBitmap()
            .load(imgUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    binding.cropImageView.setImageBitmap(resource)
                }
            })
    }

    override fun onImageClicked(imgUrl: String) {
        loadImageToCropImageView(imgUrl)
        viewModel.imgUrl = imgUrl
    }


}