package database

import androidx.room.Database

@Database(entities = [RestEntity::class],version = 1)
abstract class RestDatabase {

    abstract  fun restDao():RestDao
}