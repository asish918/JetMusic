package com.example.jetmusic.other

import com.example.jetmusic.BuildConfig
import com.example.jetmusic.data.entities.Song
import com.example.jetmusic.data.entities.SongDto

object Constants {

    const val SONG_COLLECTION = "music"
    const val SERVICE_TAG = "MusicService"

    const val MEDIA_ROOT_ID = "root_id"

    const val NETWORK_FAILURE = "NETWORK_FAILURE"

    const val UPDATE_PLAYER_POSITION_INTERVAL = 100L

    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "music"
}

fun buildSongUrl(songFileName: String) =
    "${BuildConfig.SUPABASE_URL}/storage/v1/object/public/songs/${songFileName}"

fun buildImageUrl(imageFileName: String) =
    "${BuildConfig.SUPABASE_URL}/storage/v1/object/public/images/${imageFileName}"

 fun SongDto.asDomainModel(): Song {
    return Song(
        title = this.title,
        artist = this.author,
        imageUrl = buildImageUrl(this.image),
        musicUrl = buildSongUrl(this.song),
        mediaId = this.title
    )
}