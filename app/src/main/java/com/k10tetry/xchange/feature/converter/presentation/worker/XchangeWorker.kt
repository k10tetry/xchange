package com.k10tetry.xchange.feature.converter.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.k10tetry.xchange.feature.converter.domain.usecase.BaseCurrencyRateUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.GetCurrenciesUseCase
import com.k10tetry.xchange.feature.converter.domain.usecase.GetRatesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltWorker
class XchangeWorker @AssistedInject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                coroutineScope {
                    launch { getRatesUseCase.fetchAndSaveRates() }
                    launch { getCurrenciesUseCase.fetchAndSaveCurrencies() }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@withContext Result.failure()
            }
            return@withContext Result.success()
        }
    }

}