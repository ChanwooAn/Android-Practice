package wonderwall.acw.assignment.ui.main

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wonderwall.acw.assignment.databinding.SavedImagesItemBinding
import wonderwall.acw.assignment.localDB.SavedImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SavedImagesRecyclerViewAdapter () :
RecyclerView.Adapter<SavedImagesRecyclerViewAdapter.ViewHolder>() {

    var savedImages = listOf<SavedImage>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("ImageRecycler","OnCreateView ${savedImages.first()}")
        val inflater = LayoutInflater.from(parent.context)
        val binding = SavedImagesItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedImages[position])
    }

    override fun getItemCount(): Int {
        return savedImages.size
    }



    inner class ViewHolder(private val binding: SavedImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(savedImage: SavedImage) {
            Glide
                .with(this.itemView.context)
                .load(savedImage.thumbnail)
                .centerCrop()
                .into(binding.thumbnailImageView)

            setDateAndTime(savedImage.datetime)
        }
        private fun setDateAndTime(dateAndTime:String){
            val dateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            val timeString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            binding.textDate.text = dateString
            binding.textTime.text = timeString
        }
    }
}