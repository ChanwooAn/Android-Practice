package com.acw.mygallery.imagepicker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoPickerViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    private val _photoList = MutableLiveData<List<String>>()
    val photoList: LiveData<List<String>>
        get() = _photoList

    private val _folderName = MutableLiveData<String>()
    val folderName: LiveData<String>
        get() = _folderName

    var imgUrl:String =""

    fun getPhotoList(lifecycleOwner: LifecycleOwner) {
        getImage(context, folderName.value ?: "",lifecycleOwner) { photoList ->
            _photoList.value = photoList
        }
    }

    fun setFolderName(folderName:String){
        _folderName.value=folderName
    }

}