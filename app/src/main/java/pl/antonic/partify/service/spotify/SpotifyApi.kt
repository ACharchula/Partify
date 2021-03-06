package pl.antonic.partify.service.spotify

import pl.antonic.partify.model.spotify.*
import retrofit2.Call
import retrofit2.http.*

interface SpotifyApi {

    @GET("me")
    fun getMe() : Call<User>

    @GET("me/top/artists")
    fun getTopUserArtists() : Call<ObjectList<Artist>>

    @GET("me/top/tracks")
    fun getTopUserTracks() : Call<ObjectList<Track>>

    @GET("recommendations/available-genre-seeds")
    fun getAvailableGenres() : Call<Genres>

    @GET("tracks/{id}")
    fun getTrack(@Path("id") id: String) : Call<Track>

    @GET("artists/{id}")
    fun getArtist(@Path("id") id: String) : Call<Artist>

    @GET("recommendations")
    fun getRecommendations(@Query("market") market: String,
                           @Query("limit") limit: String?,
                           @Query("target_acousticness") acousticness: String?,
                           @Query("target_danceability") danceability: String?,
                           @Query("target_energy") energy: String?,
                           @Query("target_instrumentalness") instrumentalness: String?,
                           @Query("target_popularity") popularity: String?,
                           @Query("target_valence") valence: String?,
                           @Query("seed_artists") seedArtists: String?,
                           @Query("seed_genres") seedGenres: String?,
                           @Query("seed_tracks") seedTracks: String?) : Call<Recommendations>

    @Headers("Content-Type: application/json")
    @POST("users/{user_id}/playlists")
    fun createPlaylist(@Path("user_id") userId: String, @Body data: CreatePlaylistData
    ) : Call<Playlist>

    @POST("playlists/{playlist_id}/tracks")
    fun addTracksToPlaylist(@Path("playlist_id") playlistId: String, @Body uris: TracksToAdd) : Call<Void>


    //In spotify deleting playlist is impossible - user has a feeling of deleting it, but in
    //real world it is just unfollowed - this prevents from making it impossible to reach by external
    //people who have followed it
    @DELETE("playlists/{playlist_id}/followers")
    fun unfollowPlaylist(@Path("playlist_id") playlistId: String) : Call<Void>
}