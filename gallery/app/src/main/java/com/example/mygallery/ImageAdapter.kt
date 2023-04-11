package com.example.mygallery


import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.databinding.ItemImageBinding
import com.example.mygallery.databinding.ItemLoadMoreBinding
import java.net.URI

class ImageAdapter(val itemClickListener:ItemClickListener) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem
            //주소비교
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem
            //data비교
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_IMAGE -> {
                val binding=ItemImageBinding.inflate(inflater,parent,false)
                ImageViewHolder(binding)

            }
            else -> {
                val binding=ItemLoadMoreBinding.inflate(inflater,parent,false)
                LoadMoreViewHolder(binding)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder->{
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder->{
                holder.bind(itemClickListener)
            }
        }

    }

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else {
            originSize.inc()
        }//전체 사진개수보다 하나를 더해줌으로써 마지막 item은 사진을 추가하는 버튼으로 만듬.
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount.dec() == position) ITEM_LOAD_MORE//마지막 item은 사진 추가 버튼.
        else ITEM_IMAGE
    }

    interface ItemClickListener{
        fun onLoadMoreClick()
    }
    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }

}


sealed class ImageItems {
    data class Image(
        var uri: Uri
    ) : ImageItems()

    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageItem.setImageURI(item.uri)
    }

}

class LoadMoreViewHolder(binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: ImageAdapter.ItemClickListener) {
        itemView.setOnClickListener {  itemClickListener.onLoadMoreClick() }
    }
}

