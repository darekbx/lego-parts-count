package com.darekbx.legopartscount.repository.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "defined_part")
class DefinedPartEntity(
    @PrimaryKey @ColumnInfo(name = "part_number") val partNumber: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "lego_set_defined_parts_entity")
class LegoSetDefinedPartsEntity(
    @PrimaryKey @ColumnInfo(name = "set_number") val setNumber: String,
    @ColumnInfo(name = "defined_parts_count") val definedPartsCount: Int,
    @ColumnInfo(name = "set_name") val setName: String,
    @ColumnInfo(name = "set_year") val setYear: Int,
    @ColumnInfo(name = "parts_count") val partsCount: Int,
    @ColumnInfo(name = "set_image_url") val setImageUrl: String
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

@Dao
interface LegoSetDefinedPartsDao {

    @Insert
    suspend fun insert(legoSetDefinedParts: LegoSetDefinedPartsEntity)

    @Query("SELECT set_number FROM lego_set_defined_parts_entity")
    suspend fun selectSetNumbers(): List<String>

    @Query("SELECT * FROM lego_set_defined_parts_entity")
    suspend fun selectAll(): List<LegoSetDefinedPartsEntity>
}

@Database(
    entities = arrayOf(DefinedPartEntity::class, LegoSetDefinedPartsEntity::class),
    version = 2,
    exportSchema = true
)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun definedPartsDao(): DefinedPartDao

    abstract fun legoSetDefinedPartsDao(): LegoSetDefinedPartsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `lego_set_defined_parts_entity` (
                        `set_number` TEXT NOT NULL, 
                        `defined_parts_count` INTEGER NOT NULL, 
                        `set_name` TEXT NOT NULL, 
                        `set_year` INTEGER NOT NULL, 
                        `parts_count` INTEGER NOT NULL, 
                        `set_image_url` TEXT NOT NULL, 
                        PRIMARY KEY(`set_number`)
                    )""".trimIndent())
            }
        }

    }
}
