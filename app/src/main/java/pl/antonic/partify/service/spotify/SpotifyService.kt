package pl.antonic.partify.service.spotify

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyService {
    companion object {
        private const val SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1/"

        public fun getService() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}