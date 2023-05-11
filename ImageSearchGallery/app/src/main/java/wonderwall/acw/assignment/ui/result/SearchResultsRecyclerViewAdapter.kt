package wonderwall.acw.assignment.ui.result

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wonderwall.acw.assignment.databinding.SearchResultsItemBinding
import wonderwall.acw.assignment.fetchingapi.ImageSearchResult
import wonderwall.acw.assignment.utils.isNetworkConnected
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchResultsRecyclerViewAdapter
    (
    diffUtil: DiffUtil.ItemCallback<ImageSearchResult>,
    private val onItemSaved: (ImageSearchResult) -> Unit
) :
    ListAdapter<ImageSearchResult, SearchResultsRecyclerViewAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchResultsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("SearchResultsRecyclerViewAdapter", "Binding item at position $position")
        holder.bind(getItem(position))
    }

    fun updateList(searchResults: List<ImageSearchResult>?, downloadsUrls: List<String>?) {
        if (searchResults == null) {
            return
        } else if (downloadsUrls == null) {
            submitList(searchResults)
            return
        } else {
            searchResults.forEach { imageSearchResult ->
                if (downloadsUrls.find { it == imageSearchResult.thumbnail_url } != null) {
                    imageSearchResult.isDownloaded = true
                }
            }
            submitList(searchResults)

        }
    }

    inner class ViewHolder(private val binding: SearchResultsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(searchResult: ImageSearchResult) {
            Glide
                .with(this.itemView.context)
                .load(searchResult.thumbnail_url)
                .centerCrop()
                .into(binding.thumbnailImageView)
            setDateAndTime(searchResult.datetime)

            if (searchResult.isDownloaded) {
                binding.downloadIcon.visibility = View.VISIBLE
            } else {
                binding.downloadIcon.visibility = View.INVISIBLE
            }

            binding.resultViewHolder.setOnClickListener {
                onItemSaved(searchResult)
                if (searchResult.isDownloaded) {
                    binding.downloadIcon.visibility = View.VISIBLE
                } else {
                    binding.downloadIcon.visibility = View.INVISIBLE
                }
            }
        }

        private fun setDateAndTime(dateAndTime: String) {
            val dateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            val timeString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            binding.textDate.text = dateString
            binding.textTime.text = timeString
        }
    }
}