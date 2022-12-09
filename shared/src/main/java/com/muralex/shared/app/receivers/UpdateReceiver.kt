package com.muralex.shared.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.muralex.shared.data.database.PrepopulateWorker


class UpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.MY_PACKAGE_REPLACED") {
            val populate = OneTimeWorkRequestBuilder<PrepopulateWorker>().build()
            WorkManager.getInstance(context).enqueue(populate)
        }
    }
}