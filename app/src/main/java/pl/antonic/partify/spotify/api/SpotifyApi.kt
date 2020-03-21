package pl.antonic.partify.spotify.api

import pl.antonic.partify.spotify.api.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyApi {

    @GET("me")
    public fun getMe(@Header("Authorization") token: String) : Call<User>
}