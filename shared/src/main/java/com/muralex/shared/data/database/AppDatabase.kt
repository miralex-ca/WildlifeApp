package com.muralex.shared.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.muralex.shared.app.utils.Constants.DATABASE_NAME
import com.muralex.shared.data.model.db.ArticleDataDB
import com.muralex.shared.data.model.db.ArticleSectionDataDB
import com.muralex.shared.data.model.db.DetailDataDB

@Database(
    entities = [ArticleDataDB::class, ArticleSectionDataDB::class, DetailDataDB::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val articlesDAO: ArticlesDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            prepopulate(context)
                        }
                    }
                )
                .build()
        }
    }
}


fun prepopulate(context: Context) {
    val request = OneTimeWorkRequestBuilder<PrepopulateWorker>().build()
    WorkManager.getInstance(context).enqueue(request)
}






