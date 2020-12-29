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

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/** DAO merupakan sebuah interface yang berfungsi sebagai
 * tempat deklarasi seluruh method terhadap database */
@Dao
interface VideoDao{
    @Query("select * from databasevideo")
    fun getVideos() : LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos : DatabaseVideo)
    // Kalau ada data yang primary kenya udah ada di db, maka data itu akan di replace
}

/** Class dimana sebuah db dibuat menggunakan entity dan dao yang sudah ada*/
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideoDatabase : RoomDatabase(){
    abstract val  videoDao : VideoDao
}


private lateinit var INSTANCE : VideoDatabase

fun getDatabase(context : Context) : VideoDatabase{
    synchronized(VideoDatabase::class.java){ //biar cuma ada 1 thread yang bisa akses db 1 waktu
        if (!::INSTANCE.isInitialized){ //kalau instance belum di init, buat database dulu
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideoDatabase::class.java, "videos").build()
        }
    }
    return INSTANCE
}