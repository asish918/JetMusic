package com.example.jetmusic.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val created_at: String,

    @SerialName("title")
    val title: String,

    @SerialName("author")
    val author: String,

    @SerialName("image_path")
    val image: String,

    @SerialName("song_path")
    val song: String,

    @SerialName("user_id")
    val user_id: String,
)
