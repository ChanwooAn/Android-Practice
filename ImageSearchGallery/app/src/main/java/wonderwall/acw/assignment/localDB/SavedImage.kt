package wonderwall.acw.assignment.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_images")
data class SavedImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val thumbnail: String,
    val datetime: String
)
