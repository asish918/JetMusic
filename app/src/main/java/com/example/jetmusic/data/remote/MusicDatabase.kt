package com.example.jetmusic.data.remote

import com.example.jetmusic.data.entities.Song
import com.example.jetmusic.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MusicDatabase {

    private val db = FirebaseFirestore.getInstance()

    private val songCollection = db.collection(SONG_COLLECTION)



    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection
                .get()
                .await()
                .toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}