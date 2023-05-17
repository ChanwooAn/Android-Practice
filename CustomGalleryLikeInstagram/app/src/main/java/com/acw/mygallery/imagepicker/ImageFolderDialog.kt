package com.acw.mygallery.imagepicker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.acw.mygallery.R
import com.acw.mygallery.databinding.PhotoPickerFolderListBinding
import run.acw.crew.util.imagepicker.ImageFolderListAdapter
import java.io.File

class ImageFolderDialog(): DialogFragment() {
    lateinit var folderNameList:List<String>
    private var listener: ImageFolderDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ImageFolderDialogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement DialogFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true

        setStyle(STYLE_NO_TITLE,R.style.dialog_fullscreen)
    }

    private lateinit var binding:PhotoPickerFolderListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PhotoPickerFolderListBinding.inflate(inflater, container, false)
        folderNameList=getLastNameFolderList(requireContext()).toList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.folderList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ImageFolderListAdapter(this@ImageFolderDialog::sendDataAndDismiss).apply { setFolderList(folderNameList) }
        }

    }

    // 다이얼로그에서 데이터를 전달하고자 할 때 호출하는 메서드
    private fun sendDataAndDismiss(folderName:String){
        listener?.onDialogResult(folderName)
        dismiss()
    }


    fun getLastNameFolderList(
        context: Context
    ): ArrayList<String> {

        val folderList = ArrayList<String>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val filePath = cursor.getString(columnIndex)
                val paths = filePath.split(File.separator)
                val folder = paths[paths.size - 2]
                if (!folderList.contains(folder)) {
                    folderList.add(folder)
                }
            }
            cursor.close()
        }
        return folderList
    }
}