package com.muralex.shared.data.database

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.muralex.shared.app.utils.SettingsManager
import com.muralex.shared.data.database.ArticlesDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class DBInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: ArticlesDao,
    private val settingsManager: SettingsManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val size = dao.getAllArticles().size
            settingsManager.setFirstLaunch(true)
            Timber.d("DB init: Articles count: $size")
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }
}
