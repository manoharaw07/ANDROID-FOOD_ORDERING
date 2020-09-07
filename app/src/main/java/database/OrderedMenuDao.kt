package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface OrderedMenuDao {
    @Insert
    fun insertmenu(orderedMenuEntity: OrderedMenuEntity)

    @Delete
    fun deletemenu(orderedMenuEntity: OrderedMenuEntity)

    @Query("SELECT * FROM orderedMenu")
    fun getAllmenu(): List<OrderedMenuEntity>


    @Query("SELECT * FROM orderedMenu WHERE ordered_dish_id= :menuid")
    fun getmenuById(menuid: String): OrderedMenuEntity

    @Query("DELETE FROM orderedMenu")
    fun deleteAll()
}