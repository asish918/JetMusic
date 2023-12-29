package com.example.jetmusic.data.remote

import com.example.jetmusic.BuildConfig
import com.example.jetmusic.data.entities.SongDto
import com.example.jetmusic.data.entities.Song
import com.example.jetmusic.other.asDomainModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlin.Exception

class MusicDatabase {
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
            emptyList()
        }
    }
}