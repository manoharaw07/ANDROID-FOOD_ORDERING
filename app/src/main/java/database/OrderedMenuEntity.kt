package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderedMenu")
data class OrderedMenuEntity(
    @PrimaryKey val ordered_dish_id: String,
    @ColumnInfo(name = "dish_name") val dishName: String,
    @ColumnInfo(name = "cost") val cost: String

)