package com.muralex.shared.app.di

import android.content.Context
import com.muralex.shared.data.database.AppDatabase
import com.muralex.shared.data.database.ArticlesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context) : AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Singleton
    @Provides
    fun providesDao(database: AppDatabase) : ArticlesDao {
        return database.articlesDAO
    }

}