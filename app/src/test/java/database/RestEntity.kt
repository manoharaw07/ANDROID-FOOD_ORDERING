package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurents")
data class RestEntity (
    @PrimaryKey val rest_id:Int,
    @ColumnInfo(name = "restaurent_name") val restName:String,
    @ColumnInfo(name = "restaurent_cost") val restCost:String,
    @ColumnInfo(name = "restaurent_rating") val restRating:String,
    @ColumnInfo(name = "restaurent_img") val restImg:String
)