package wonderwall.acw.assignment.localDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedImage::class], version = 1)
abstract class ImageDataBase : RoomDatabase() {
    abstract fun savedImageDao(): SavedImageDao
}