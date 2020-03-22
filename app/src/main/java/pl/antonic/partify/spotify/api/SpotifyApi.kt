package pl.antonic.partify.spotify.api

import pl.antonic.partify.spotify.api.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyApi {

    @GET("me")
    public fun getMe(@Header("Authorization") token: String) : Call<User>

    @GET("me/top/artists")
    public fun getTopUserArtists(@Header("Authorization") token: String) : Call<ObjectList<Artist>>

    @GET("me/top/tracks")
    public fun getTopUserTracks(@Header("Authorization") token: String) : Call<ObjectList<Track>>

    @GET("recommendations/available-genre-seeds")
    public fun getAvailableGenres(@Header("Authorization") token: String) : Call<Genres>
}