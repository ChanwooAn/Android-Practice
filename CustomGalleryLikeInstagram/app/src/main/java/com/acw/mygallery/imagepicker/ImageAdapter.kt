package run.acw.crew.util.imagepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.acw.mygallery.databinding.RecyclerViewItemImagePickerBinding
import com.bumptech.glide.Glide

class ImageAdapter() : RecyclerView.Adapter<ImageViewHolder>(){
    var picturePath = mutableListOf<String>()

    fun setPicturePaths(list : List<String>){
        picturePath.addAll(list)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RecyclerViewItemImagePickerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return picturePath.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide
            .with(holder.itemView.context)
            .load(picturePath[position])
            .centerCrop()
            .into(holder.itemView.rootView as ImageView);

    }
}

class ImageViewHolder(private val binding: RecyclerViewItemImagePickerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
    }
}
