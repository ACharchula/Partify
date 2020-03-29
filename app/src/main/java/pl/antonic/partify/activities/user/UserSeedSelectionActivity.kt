package pl.antonic.partify.activities.user

import android.content.Intent
import android.widget.ListView
import pl.antonic.partify.list.adapters.ArtistListAdapter
import pl.antonic.partify.list.adapters.GenreListAdapter
import pl.antonic.partify.list.adapters.TrackListAdapter
import pl.antonic.partify.activities.SeedSelectionActivity
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.Artist
import pl.antonic.partify.spotify.api.model.Genres
import pl.antonic.partify.spotify.api.model.ObjectList
import pl.antonic.partify.spotify.api.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class UserSeedSelectionActivity : SeedSelectionActivity() {

    override fun setTrackData(listView: ListView) {
        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getTopUserTracks("Bearer " + TokenService.retrieve(this))
        call.enqueue(object : Callback<ObjectList<Track>> {
            override fun onFailure(call: Call<ObjectList<Track>>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<ObjectList<Track>>, response: Response<ObjectList<Track>>) {
                val result = response.body()
                val adapter = TrackListAdapter(
                    this@UserSeedSelectionActivity,
                    result!!
                )
                listView.adapter = adapter
            }
        })
    }

    override fun setArtistData(listView: ListView) {
        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getTopUserArtists("Bearer " + TokenService.retrieve(this.applicationContext))
        call.enqueue(object : Callback<ObjectList<Artist>> {
            override fun onFailure(call: Call<ObjectList<Artist>>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<ObjectList<Artist>>, response: Response<ObjectList<Artist>>) {
                val result = response.body()
                val adapter = ArtistListAdapter(
                    this@UserSeedSelectionActivity,
                    result!!
                )
                listView.adapter = adapter
            }

        })
    }

    override fun setGenreData(listView: ListView) {
        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getAvailableGenres("Bearer " + TokenService.retrieve(this.applicationContext))
        call.enqueue(object : Callback<Genres> {
            override fun onFailure(call: Call<Genres>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                val result = response.body()
                val adapter = GenreListAdapter(
                    this@UserSeedSelectionActivity,
                    result!!
                )
                listView.adapter = adapter
            }

        })
    }

    override fun nextButtonOnClick() {
        val intent = Intent(this, UserFinalActivity::class.java)
        startActivity(intent)
    }
}
