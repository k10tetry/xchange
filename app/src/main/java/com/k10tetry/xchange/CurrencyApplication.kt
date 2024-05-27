package com.k10tetry.xchange

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.k10tetry.xchange.feature.converter.presentation.worker.XchangeWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.content.res.Configuration as ResourceConfigs

@HiltAndroidApp
class CurrencyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO).build()

    override fun onCreate() {
        super.onCreate()

        setAppTheme()
        setWorker()
    }

    private fun setWorker() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            XchangeWorker::class.java,
            2 * PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            XCHANGE_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun setAppTheme() {
        val mode =
            if ((resources.configuration.uiMode and ResourceConfigs.UI_MODE_NIGHT_MASK) == ResourceConfigs.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {
        const val XCHANGE_WORKER = "xchange_worker"
    }

}