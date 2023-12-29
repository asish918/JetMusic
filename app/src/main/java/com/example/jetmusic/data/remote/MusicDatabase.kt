package com.example.jetmusic.data.remote

import android.util.Log
import com.example.jetmusic.BuildConfig
import com.example.jetmusic.data.entities.SongDto
import com.example.jetmusic.data.entities.Song
//import com.example.jetmusic.other.Constants.SONG_COLLECTION
import com.example.jetmusic.other.asDomainModel
//import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.selects.select
import timber.log.Timber
import kotlin.Exception

class MusicDatabase {

//    private val db = FirebaseFirestore.getInstance()
//    private val songCollection = db.collection(SONG_COLLECTION)

    private fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.API_KEY
        ) {
            install(Postgrest)
        }
    }
    
    private val client = provideSupabaseClient()
    private fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    suspend fun getAllSongs(): List<Song> {
        return try {
            provideSupabaseDatabase(client).from("songs")
                .select().decodeList<SongDto>()
                .map { it.asDomainModel() }
        } catch (e: Exception) {
            Timber.tag("Bimch").i(e.toString())
            emptyList()
        }
    }

//    suspend fun getAllSongs(): List<Song> {
//        return try {
//            songCollection
//                .get()
//                .await()
//                .toObjects(Song::class.java)
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
}