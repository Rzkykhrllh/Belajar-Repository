package com.example.android.devbyteviewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.devbyteviewer.domain.Video
import kotlinx.android.synthetic.main.devbyte_item.view.*

/** Bentuk entity pada database room/local
 * yang akan menjadi atribut2 pada tabel/DB */

@Entity
data class DatabaseVideo constructor(
        @PrimaryKey
        val url: String,
        val updated : String,
        val title: String,
        val description : String,
        val thumbanil : String
)
    // Fungsi untuk konversi bentuk dari DatabaseVideo ke Video
    fun List<DatabaseVideo>.asDomainModel() : List<Video>{
        return map{
            Video (
                url = it.url,
                title = it.title,
                description  = it.description,
                updated = it.updated,
                thumbnail = it.thumbanil
            )
        }
    }


