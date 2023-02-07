package com.aferrari.login.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    version = 6,
    entities = [Leader::class, Trainee::class, Material::class, Category::class],
    autoMigrations = [
        AutoMigration(from = 5, to = 6, spec = UserDataBase.AutoMigration5To6::class)
    ],
    exportSchema = true
)
abstract class UserDataBase : RoomDatabase() {

    @DeleteTable(tableName = "user_data_table")
    class AutoMigration1To2 : AutoMigrationSpec

    class AutoMigration2To3 : AutoMigrationSpec

    class AutoMigration3To4 : AutoMigrationSpec

    class AutoMigration4To5 : AutoMigrationSpec

    class AutoMigration5To6 : AutoMigrationSpec

    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun getInstance(context: Context): UserDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDataBase::class.java,
                        "user_data_database"
                    )
                        .addMigrations(MIGRATION_4_5, MIGRATION_5_6)
                        .build()
                }
                return instance
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE category_data_table "
                            + " ADD COLUMN leader_category_id INTEGER"
                )
            }
        }

        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE material_data_table "
                            + " ADD COLUMN leader_material_id INTEGER"
                )
            }
        }
    }

}