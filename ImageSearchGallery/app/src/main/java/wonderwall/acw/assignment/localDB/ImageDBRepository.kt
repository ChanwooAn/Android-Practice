package wonderwall.acw.assignment.localDB

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch
import wonderwall.acw.assignment.fetchingapi.ImageSearchResult

private const val DATABASE_NAME = "image-database"

class ImageDBRepository private constructor(context: Context) {

    //database initialize
    private val database: ImageDataBase = Room.databaseBuilder(
        context.applicationContext, //database가 사용될 context(application의 context)
        ImageDataBase::class.java, // room으로 생성하고자하는 database class
        DATABASE_NAME // db이름
    ).build()

    private val imageDao = database.savedImageDao()

    fun getSavedImages(): LiveData<List<SavedImage>> = imageDao.getAllSavedImages().asLiveData()
    suspend fun saveSearchResult(selectedImage: ImageSearchResult) {
        val img =
            SavedImage(thumbnail = selectedImage.thumbnail_url, datetime = selectedImage.datetime)
        imageDao.insert(img)
    }


    companion object {
        private var INSTANCE: ImageDBRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ImageDBRepository(context);
            }

        }

        fun get(): ImageDBRepository {
            return INSTANCE ?: throw IllegalStateException("ImageDBRepository must be initialized")
        }
    }
}