package com.muralex.navstructure.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.muralex.navstructure.BuildConfig
import com.muralex.shared.data.database.DBInitWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        val dbInit = OneTimeWorkRequestBuilder<DBInitWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(dbInit)


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            /// populate on each app start in debug version
//            val populate = OneTimeWorkRequestBuilder<PrepopulateWorker>().build()
//            WorkManager.getInstance(applicationContext).enqueue(populate)
        }
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            //.setMinimumLoggingLevel(Log.DEBUG)
            .build()

}
