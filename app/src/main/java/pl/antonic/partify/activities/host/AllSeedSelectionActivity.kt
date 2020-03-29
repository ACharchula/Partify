package pl.antonic.partify.activities.host

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import pl.antonic.partify.*
import pl.antonic.partify.activities.SeedSelectionActivity
import pl.antonic.partify.list.adapters.ArtistListAdapter
import pl.antonic.partify.list.adapters.GenreListAdapter
import pl.antonic.partify.list.adapters.TrackListAdapter
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

class AllSeedSelectionActivity : SeedSelectionActivity() {

    private lateinit var allSeeds : Seeds

    override fun getExtraData() {
        allSeeds = intent.getSerializableExtra("all_seeds") as Seeds
    }

    override fun setTrackData(listView: ListView) {
        val trackList = mutableListOf<Track>()

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)

        val allTracksSeeds = allSeeds.getList(SeedType.TRACK)
        for (trackId in allTracksSeeds) {
            val call = apiService.getTrack("Bearer " + TokenService.retrieve(this), trackId)
            call.enqueue(object : Callback<Track> {
                override fun onFailure(call: Call<Track>, t: Throwable) {
                    val a = 1
                }

                override fun onResponse(call: Call<Track>, response: Response<Track>) {
                    trackList.add(response.body()!!)

                    if (trackList.size == allTracksSeeds.size) {
                        val adapter = TrackListAdapter(this@AllSeedSelectionActivity, ObjectList(trackList))
                        listView.adapter = adapter
                    }
                }
            })
        }
    }

    override fun setArtistData(listView: ListView) {
        val artistList = mutableListOf<Artist>()

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)

        val allArtistsSeeds = allSeeds.getList(SeedType.ARTIST)

        for (artistId in allArtistsSeeds) {
            val call = apiService.getArtist("Bearer " + TokenService.retrieve(this), artistId)
            call.enqueue(object : Callback<Artist> {
                override fun onFailure(call: Call<Artist>, t: Throwable) {
                    val a = 1
                }

                override fun onResponse(call: Call<Artist>, response: Response<Artist>) {
                    artistList.add(response.body()!!)

                    if (artistList.size == allArtistsSeeds.size) {
                        val adapter = ArtistListAdapter(this@AllSeedSelectionActivity, ObjectList(artistList))
                        listView.adapter = adapter
                    }
                }
            })
        }
    }

    override fun setGenreData(listView: ListView) {
        val adapter = GenreListAdapter(this, Genres(allSeeds.getList(SeedType.GENRE)))
        listView.adapter = adapter
    }

    override fun nextButtonOnClick() {
        val intent = Intent(this, PlaylistActivity::class.java)
        intent.putExtra("final_seeds", getSeeds())
        startActivity(intent)
    }

}
