/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.android.devbyteviewer.work.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Basicly ini class yang paling dijalanin duluan sebelum activity manapun
 */
class DevByteApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    // fungsi request worknya
    private fun setupRecurringWork() {

        // deklarasi reques pada request
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // apapun kondisi internetnya
                .setRequiresBatteryNotLow(true) // kalau batrainya gak rendah
                .setRequiresCharging(true) // bisa kalau di charge
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        setRequiresDeviceIdle(true) // kalau idle
                    }
                }.build()

        // inisiasi requestnya
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(
                1,
                TimeUnit.DAYS
        ).setConstraints(constraints)
                .build()

        // manggil worknya secara periodik dan unik
        WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshDataWork.WORK_NAME, // kalau namanya sama
                ExistingPeriodicWorkPolicy.KEEP, // yang lama di keep
                repeatingRequest // requestnya make repating request
        )
    }

}
