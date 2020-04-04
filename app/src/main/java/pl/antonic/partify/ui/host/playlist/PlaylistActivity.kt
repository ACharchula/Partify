package pl.antonic.partify.ui.host.playlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.spotify.*
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyApi
import pl.antonic.partify.service.spotify.SpotifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistActivity : AppCompatActivity() {

    lateinit var finalSeeds : Seeds
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        finalSeeds = intent.getSerializableExtra("final_seeds") as Seeds

        listView = findViewById(R.id.playlistListView)

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)

        val seedArtists: String? = nullOrJoinedElements(finalSeeds.getList(SeedType.ARTIST))
        val seedTracks: String? = nullOrJoinedElements(finalSeeds.getList(SeedType.TRACK))
        val seedGenres: String? = nullOrJoinedElements(finalSeeds.getList(SeedType.GENRE))

        //TODO change market to get from user
        val call = apiService.getRecommendations("Bearer " + TokenService.retrieve(this)
            , "PL", seedArtists, seedGenres, seedTracks)

        call.enqueue(object : Callback<Recommendations> {
            override fun onFailure(call: Call<Recommendations>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<Recommendations>, response: Response<Recommendations>) {
                val recommendations = response.body()!!

                //TODO check if tracks can be null
                createPlaylistAndAddTracks(recommendations.tracks!!)
                val adapter =
                    PlaylistTracksListAdapter(
                        this@PlaylistActivity,
                        ObjectList(
                            recommendations.tracks!!
                        )
                    )
                listView.adapter = adapter
            }
        })
    }

    private fun createPlaylistAndAddTracks(tracks: List<Track>) {

        val tracksURIs = mutableListOf<String>()

        for (track in tracks) {
            tracksURIs.add(track.uri!!)
        }

        val tracksString = tracksURIs.joinToString(",")

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)

        val call = apiService.getMe("Bearer " + TokenService.retrieve(this))
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()!!
                val secondCall = apiService.createPlaylist("Bearer " + TokenService.retrieve(this@PlaylistActivity),
                    user.id!!,
                    CreatePlaylistData("Partify-" + user.display_name + " " + System.currentTimeMillis())
                )

                secondCall.enqueue(object : Callback<Playlist> {
                    override fun onFailure(call: Call<Playlist>, t: Throwable) {
                        val a = 1
                    }

                    override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                        val playlist = response.body()!!
                        val thirdCallback = apiService.addTracksToPlaylist("Bearer " + TokenService.retrieve(this@PlaylistActivity)
                            , playlist.id!!, tracksString)
                        thirdCallback.enqueue(object : Callback<Void> {
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@PlaylistActivity, "NOT DONE", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                Toast.makeText(this@PlaylistActivity, "DONE", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }

                })
            }

        })

    }

    private fun nullOrJoinedElements(list: List<String>) : String? {
        return if (list.isEmpty()) null else list.joinToString(",")
    }

}
