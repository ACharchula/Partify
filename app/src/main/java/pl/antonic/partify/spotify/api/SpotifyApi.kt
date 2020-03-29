package pl.antonic.partify.spotify.api

import pl.antonic.partify.spotify.api.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("me")
    public fun getMe(@Header("Authorization") token: String) : Call<User>

    @GET("me/top/artists")
    public fun getTopUserArtists(@Header("Authorization") token: String) : Call<ObjectList<Artist>>

    @GET("me/top/tracks")
    public fun getTopUserTracks(@Header("Authorization") token: String) : Call<ObjectList<Track>>

    @GET("recommendations/available-genre-seeds")
    public fun getAvailableGenres(@Header("Authorization") token: String) : Call<Genres>

    //TODO maybe better to use tracks with several ids - maximum ids number is 50 - same with artists

    @GET("tracks/{id}")
    public fun getTrack(@Header("Authorization") token: String, @Path("id") id: String) : Call<Track>

    @GET("artists/{id}")
    public fun getArtist(@Header("Authorization") token: String, @Path("id") id: String) : Call<Artist>

    @GET("recommendations")
    public fun getRecommendations(@Header("Authorization") token: String,
//                                  @Query("market") market: String,
                                  @Query("seed_artists") seedArtists: String,
                                  @Query("seed_genres") seedGenres: String,
                                  @Query("seed_tracks") seedTracks: String) : Call<Recommendations>
}