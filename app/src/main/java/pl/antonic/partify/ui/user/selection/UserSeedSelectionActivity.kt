package pl.antonic.partify.ui.user.selection

import android.content.Intent
import android.widget.ListView
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.ui.common.selection.artists.ArtistListAdapter
import pl.antonic.partify.ui.common.selection.genres.GenreListAdapter
import pl.antonic.partify.ui.common.selection.tracks.TrackListAdapter
import pl.antonic.partify.ui.common.selection.SeedSelectionActivity
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyApi
import pl.antonic.partify.service.spotify.SpotifyService
import pl.antonic.partify.model.spotify.Artist
import pl.antonic.partify.model.spotify.Genres
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track
import pl.antonic.partify.ui.user.last.UserLastActivity
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
                val adapter =
                    TrackListAdapter(
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
                val adapter =
                    ArtistListAdapter(
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
                val adapter =
                    GenreListAdapter(
                        this@UserSeedSelectionActivity,
                        result!!
                    )
                listView.adapter = adapter
            }

        })
    }

    override fun nextButtonOnClick() {
        val bytesPayload = Payload.fromBytes(Gson().toJson(getSeeds()).toByteArray())
        NearbyClientService.get().sendPayload(intent.getStringExtra("endpointId")!!, bytesPayload)

        val intent = Intent(this, UserLastActivity::class.java)
        startActivity(intent)
    }
}
