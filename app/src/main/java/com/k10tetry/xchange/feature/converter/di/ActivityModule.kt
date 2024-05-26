package com.k10tetry.xchange.feature.converter.di

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.k10tetry.xchange.feature.converter.di.qualifier.GridLayout
import com.k10tetry.xchange.feature.converter.di.qualifier.LinearLayout
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Named
import javax.inject.Qualifier

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @GridLayout
    fun provideGridLayoutManager(@ActivityContext context: Context): LayoutManager =
        GridLayoutManager(context, 2)

    @Provides
    @LinearLayout
    fun provideLinearLayoutManager(@ActivityContext context: Context): LayoutManager =
        LinearLayoutManager(context)

}