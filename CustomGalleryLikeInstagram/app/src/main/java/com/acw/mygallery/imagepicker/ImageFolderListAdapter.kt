package run.acw.crew.util.imagepicker

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acw.mygallery.databinding.RecyclerViewItemPhotoPickerFolderListBinding

class ImageFolderListAdapter(private val onClick:(folderName:String)->Unit) :
    RecyclerView.Adapter<ImageFolderListAdapter.FolderHolder>(){

    data class FolderItem(val name:String,val thumbnail: Uri)

    private var folderNameList= mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val binding = RecyclerViewItemPhotoPickerFolderListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderHolder(binding)
    }
    fun setFolderList(folderList:List<String>){
        folderNameList.clear()
        folderNameList.addAll(folderList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return folderNameList.size
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        holder.bind(folderNameList[position])
    }

    inner class FolderHolder(private val binding: RecyclerViewItemPhotoPickerFolderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onClick(binding.folderName.text.toString())
            }
        }
        fun bind(item: String) {
            binding.folderName.text=item
        }
    }

}

