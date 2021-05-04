package com.darekbx.legopartscount.repository.database

import android.content.Context
import androidx.room.*

@Entity(tableName = "defined_part")
class DefinedPartEntity(
    @PrimaryKey @ColumnInfo(name = "part_number") val partNumber: Int,
)

@Dao
interface DefinedPartDao {

    @Insert
    suspend fun insert(definedPartEntities: List<DefinedPartEntity>)

    @Query("SELECT part_number FROM defined_part")
    suspend fun selectAll(): List<DefinedPartEntity>

    @Delete
    suspend fun delete(partEntityNumber: DefinedPartEntity)
}

@Database(entities = arrayOf(DefinedPartEntity::class), version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun definedPartsDao(): DefinedPartDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
