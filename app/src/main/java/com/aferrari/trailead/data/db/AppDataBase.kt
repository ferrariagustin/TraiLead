package com.aferrari.trailead.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aferrari.trailead.data.apiservices.LocalDataSourceDao
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Pdf
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import javax.inject.Singleton


@Database(
    version = 2,
    entities = [Leader::class, Trainee::class, YouTubeVideo::class, Category::class, TraineeCategoryJoin::class, Link::class, Pdf::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AppDataBase.AutoMigration1To2::class)
    ],
    exportSchema = true
)
@Singleton
abstract class AppDataBase : RoomDatabase() {

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
    class AutoMigration1To2 : AutoMigrationSpec

    abstract val localDataSourceDao: LocalDataSourceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "user_data_database"
                    )
//                        .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                        .fallbackToDestructiveMigration()
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