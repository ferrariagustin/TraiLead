package com.aferrari.login.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 4,
    entities = [Leader::class, Trainee::class, Material::class, Category::class],
    autoMigrations = [
        AutoMigration(from = 3, to = 4, spec = UserDataBase.AutoMigration3To4::class)
    ],
    exportSchema = true
)
abstract class UserDataBase : RoomDatabase() {

    @DeleteTable(tableName = "user_data_table")
    class AutoMigration1To2 : AutoMigrationSpec

    class AutoMigration2To3 : AutoMigrationSpec

    class AutoMigration3To4 : AutoMigrationSpec

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
                        .build()
                }
                return instance
            }
        }
    }

}