package wonderwall.acw.assignment.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import wonderwall.acw.assignment.localDB.ImageDBRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val savedImages = ImageDBRepository.get().getSavedImages()

}