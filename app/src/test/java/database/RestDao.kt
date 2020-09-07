package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestDao {
    @Insert
    fun insertRest(restEntity: RestEntity)

    @Delete
    fun deleteRest(restEntity: RestEntity)

    @Query("SELECT * FROM restaurents")
    fun getAllRest():List<RestEntity>

    @Query("SELECT * FROM restaurents WHERE rest_id=:id")
    fun getRestById(id:String):RestEntity
}
