package database

import androidx.room.Database

import androidx.room.RoomDatabase

@Database(entities = [OrderedMenuEntity::class], version = 1)
abstract class OrderedMenuDatabase (): RoomDatabase()
{
   abstract fun orderedMenuDao():OrderedMenuDao
}
