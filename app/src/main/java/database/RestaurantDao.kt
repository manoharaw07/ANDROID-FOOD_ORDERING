package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import database.RestaurantEntity

@Dao
interface RestaurantDao {

    @Insert
    fun insertRes(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRes(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    fun getAllRes(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE res_id= :resId")
    fun getRestById(resId: String): RestaurantEntity
}