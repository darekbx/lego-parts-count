package com.darekbx.legopartscount.repository.database

import android.content.Context
import androidx.room.*

@Entity(tableName = "defined_part")
class DefinedPartEntity(
    @PrimaryKey @ColumnInfo(name = "part_number") val partNumber: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "name") val name: String
)

@Dao
interface DefinedPartDao {

    @Insert
    suspend fun insert(definedPartEntities: List<DefinedPartEntity>)

    @Query("SELECT * FROM defined_part")
    suspend fun selectAll(): List<DefinedPartEntity>

    @Query("DELETE FROM defined_part WHERE part_number = :partNumber")
    suspend fun delete(partNumber: Int)
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
