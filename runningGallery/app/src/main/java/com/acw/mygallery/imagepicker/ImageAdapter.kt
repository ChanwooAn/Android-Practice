package run.acw.crew.util.imagepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.acw.mygallery.databinding.RecyclerViewItemImagePickerBinding
import com.bumptech.glide.Glide

class ImageAdapter(private val listener:OnImageClicked) : RecyclerView.Adapter<ImageViewHolder>(){
    interface OnImageClicked{
        fun onImageClicked(imgUrl:String)
    }
    var picturePath = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setPicturePaths(list : List<String>){
        picturePath.clear()
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

        holder.bind(picturePath[position],listener)

    }
}

class ImageViewHolder(private val binding: RecyclerViewItemImagePickerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String,listener: ImageAdapter.OnImageClicked) {
        Glide
        .with(itemView.context)
        .load(item)
        .centerCrop()
        .into(itemView.rootView as ImageView)

        itemView.setOnClickListener {
            listener.onImageClicked(item)
        }
    }
}
