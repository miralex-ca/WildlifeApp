package com.muralex.shared.app.di

import com.muralex.shared.app.utils.DataDelayProvider
import com.muralex.shared.app.utils.Dispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDispatchers() : Dispatchers = Dispatchers.Base()

    @Singleton
    @Provides
    fun provideDelayProvide() : DataDelayProvider = DataDelayProvider.Base()

}