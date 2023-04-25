package com.aferrari.login.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aferrari.login.data.material.YouTubeVideo


@Database(
    version = 8,
    entities = [Leader::class, Trainee::class, YouTubeVideo::class, Category::class, TraineeCategoryJoin::class],
    autoMigrations = [
        AutoMigration(from = 7, to = 8, spec = UserDataBase.AutoMigration7To8::class)
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

    class AutoMigration6To7 : AutoMigrationSpec

    @RenameTable(fromTableName = "material_data_table", toTableName = "youtube_video_data_table")
    @RenameColumn(
        tableName = "material_data_table",
        fromColumnName = "material_title",
        toColumnName = "title"
    )
    @RenameColumn(
        tableName = "material_data_table",
        fromColumnName = "material_id",
        toColumnName = "id"
    )
    @RenameColumn(
        tableName = "material_data_table",
        fromColumnName = "material_url",
        toColumnName = "url"
    )
    @RenameColumn(
        tableName = "material_data_table",
        fromColumnName = "category_material_id",
        toColumnName = "categoryId"
    )
    @RenameColumn(
        tableName = "material_data_table",
        fromColumnName = "leader_material_id",
        toColumnName = "leaderMaterialId"
    )
    class AutoMigration7To8 : AutoMigrationSpec

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
                        .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
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

        private val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `trainee_category_join` " +
                            "(`trainee_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, " +
                            "PRIMARY KEY(`trainee_id`, `category_id`), FOREIGN KEY(`trainee_id`) " +
                            "REFERENCES `trainee_data_table`(`trainee_id`) ON UPDATE NO ACTION ON DELETE NO ACTION , " +
                            "FOREIGN KEY(`category_id`) REFERENCES `category_data_table`(`category_id`) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION )"
                );
            }
        }

        private val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `trainee_category_join` " +
                            "(`trainee_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, " +
                            "PRIMARY KEY(`trainee_id`, `category_id`), FOREIGN KEY(`trainee_id`) " +
                            "REFERENCES `trainee_data_table`(`trainee_id`) ON UPDATE NO ACTION ON DELETE NO ACTION , " +
                            "FOREIGN KEY(`category_id`) REFERENCES `category_data_table`(`category_id`) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION )"
                );
            }
        }
    }

}