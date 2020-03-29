package pl.antonic.partify.spotify.api

import pl.antonic.partify.spotify.api.model.*
import retrofit2.Call
import retrofit2.http.*

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
                                  @Query("market") market: String,
                                  @Query("seed_artists") seedArtists: String?,
                                  @Query("seed_genres") seedGenres: String?,
                                  @Query("seed_tracks") seedTracks: String?) : Call<Recommendations>

    @Headers("Content-Type: application/json")
    @POST("users/{user_id}/playlists")
    public fun createPlaylist(@Header("Authorization") token: String,
                              @Path("user_id") userId: String,
                              @Body data: CreatePlaylistData) : Call<Playlist>

    @POST("playlists/{playlist_id}/tracks")
    public fun addTracksToPlaylist(@Header("Authorization") token: String,
                              @Path("playlist_id") playlistId: String,
                              @Query("uris") tracks: String) : Call<Void>


    //In spotify deleting playlist is impossible - user has a feeling of deleting it, but in
    //real world it is just unfollowed - this prevents from making it impossible to reach by external
    //people who have followed it
    @DELETE("playlists/{playlist_id}/followers")
    public fun unfollowPlaylist(@Header("Authorization") token: String,
                                   @Path("playlist_id") playlistId: String) : Call<Void>


}