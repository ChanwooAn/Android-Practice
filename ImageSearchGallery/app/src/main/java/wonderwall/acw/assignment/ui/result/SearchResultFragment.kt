package wonderwall.acw.assignment.ui.result

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wonderwall.acw.assignment.databinding.FragmentSearchResultBottomSheetBinding
import wonderwall.acw.assignment.fetchingapi.ImageSearchResult
import wonderwall.acw.assignment.utils.isNetworkConnected

class SearchResultFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchResultBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by lazy {
        ViewModelProvider(this)[SearchResultViewModel::class.java]
    }
    private val searchResultsAdapter by lazy {
        SearchResultsRecyclerViewAdapter(diffUtil) { searchResult ->
            viewModel.saveSearchResult(searchResult)
            searchResult.isDownloaded = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBottomSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        return root
    }

    private fun setupRecyclerView() {
        binding.searchResultsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchResultsAdapter
        }

    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = binding.searchEditText.text.toString()
                viewModel.search(query)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            Log.d(TAG, searchResults.toString())
            searchResultsAdapter.updateList(
                searchResults,
                viewModel.downloadedUrls.value?.map { it.thumbnail })
        }
        viewModel.downloadedUrls.observe(viewLifecycleOwner) { downloadedUrls ->
            Log.d(TAG, downloadedUrls.toString())
            searchResultsAdapter.updateList(
                viewModel.searchResults.value,
                downloadedUrls.map { it.thumbnail })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        val TAG = "SearchResultFragment"
        private val diffUtil = object : DiffUtil.ItemCallback<ImageSearchResult>() {
            override fun areItemsTheSame(
                oldItem: ImageSearchResult,
                newItem: ImageSearchResult
            ): Boolean {
                return oldItem.thumbnail_url == newItem.thumbnail_url
            }

            override fun areContentsTheSame(
                oldItem: ImageSearchResult,
                newItem: ImageSearchResult
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}