package com.muralex.navstructure.app.di


import com.muralex.navstructure.presentation.utils.DelayProvider
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
    fun provideDelayProvide() : DelayProvider = DelayProvider.Base()


}