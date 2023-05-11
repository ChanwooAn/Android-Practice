package wonderwall.acw.assignment.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wonderwall.acw.assignment.fetchingapi.ImageCacher
import wonderwall.acw.assignment.fetchingapi.ImageSearchResult
import wonderwall.acw.assignment.localDB.ImageDBRepository

class SearchResultViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchResults = MutableLiveData<List<ImageSearchResult>>()
    val searchResults: LiveData<List<ImageSearchResult>>
        get() = _searchResults
    val downloadedUrls = ImageDBRepository.get().getSavedImages()


    fun search(query: String) {
        viewModelScope.launch {
            _searchResults.value =
                ImageCacher.get().search(query, getApplication<Application>().applicationContext)
        }
    }

    fun saveSearchResult(searchResult: ImageSearchResult) {
        viewModelScope.launch {
            ImageDBRepository.get().saveSearchResult(searchResult)
        }
    }
}