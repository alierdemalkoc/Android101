package com.alierdemalkoc.countries.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class MySharedPreferences {
    companion object {
        private val PREFERENCES_TIME = "preferences_time"
        private var sharedPreferences: SharedPreferences? = null
        @Volatile private var instance: MySharedPreferences? = null
        private val lock = Any()
        operator fun invoke(context: Context): MySharedPreferences = instance ?: synchronized(lock){
            instance ?: makeSharedPreferences(context).also {
                instance = it
            }
        }
        private fun makeSharedPreferences(context: Context): MySharedPreferences{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return MySharedPreferences()
        }
    }
    fun saveTime(time: Long){
        sharedPreferences?.edit(commit = true){
            putLong(PREFERENCES_TIME,time)
        }
    }
    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME,0)
}