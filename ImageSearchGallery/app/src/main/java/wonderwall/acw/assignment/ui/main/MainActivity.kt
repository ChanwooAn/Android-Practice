package wonderwall.acw.assignment.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wonderwall.acw.assignment.R
import wonderwall.acw.assignment.ui.result.SearchResultFragment
import wonderwall.acw.assignment.databinding.ActivityMainBinding
import wonderwall.acw.assignment.fetchingapi.ImageCacher
import wonderwall.acw.assignment.fetchingapi.ImageFetcher
import wonderwall.acw.assignment.localDB.ImageDBRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val savedImagesAdapter by lazy {
        SavedImagesRecyclerViewAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSingletons()
        setUpImageRecyclerView()
        observeViewModel()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_layout, SearchResultFragment())
            .commit()

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }


    }

    private fun setUpImageRecyclerView() {
        binding.savedImageRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = savedImagesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.savedImages.observe(this) { savedImages ->
            savedImagesAdapter.savedImages = savedImages
        }
    }

    private fun initializeSingletons() {
        ImageFetcher.initialize()
        ImageCacher.initialize()
        ImageDBRepository.initialize(this)
    }


}