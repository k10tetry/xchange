package com.k10tetry.xchange.feature.converter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import com.k10tetry.xchange.BuildConfig
import com.k10tetry.xchange.feature.converter.data.local.XchangeDataStore
import com.k10tetry.xchange.feature.converter.data.remote.XchangeCurrencyApi
import com.k10tetry.xchange.feature.converter.data.repository.XchangeLocalRepositoryImpl
import com.k10tetry.xchange.feature.converter.data.repository.XchangeRemoteRepositoryImpl
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeLocalRepository
import com.k10tetry.xchange.feature.converter.domain.repository.XchangeRemoteRepository
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeDispatcher
import com.k10tetry.xchange.feature.converter.presentation.utils.XchangeDispatcherImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApi(okHttpClient: OkHttpClient): XchangeCurrencyApi {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(XchangeCurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    }

    @Provides
    @Singleton
    fun provideXchangeRemoteRepository(xchangeCurrencyApi: XchangeCurrencyApi): XchangeRemoteRepository {
        return XchangeRemoteRepositoryImpl(xchangeCurrencyApi)
    }

    @Provides
    @Singleton
    fun provideXchangeLocalRepository(xchangeDataStore: XchangeDataStore): XchangeLocalRepository {
        return XchangeLocalRepositoryImpl(xchangeDataStore)
    }

    @Provides
    @Singleton
    fun provideDispatchers(): XchangeDispatcher {
        return XchangeDispatcherImpl()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(BuildConfig.DATASTORE_FILE)
        }
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }


}