package com.android.nobetcieczane.di

import android.content.Context
import android.content.SharedPreferences
import com.android.nobetcieczane.common.Constants
import com.android.nobetcieczane.util.MyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMySharedPreferenceManager(sharedPreferences: SharedPreferences) =
        MyPreferences(sharedPreferences)
}