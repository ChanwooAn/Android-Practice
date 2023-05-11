package wonderwall.acw.assignment.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedImageDao {
    @Insert
    suspend fun insert(savedImage: SavedImage)

    @Query("SELECT * FROM saved_images ORDER BY id ASC")
    fun getAllSavedImages(): Flow<List<SavedImage>>
}