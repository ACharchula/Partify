package pl.antonic.partify.ui.host.playlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_playlist.*
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
    lateinit var playlist : Playlist
    var connected = false

    val CLIENT_ID = "871a79f969aa43f4923bfa59a852d6fe"
    val REDIRECT_URI = "partify://callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    lateinit var playlistTrackListAdapter : PlaylistTracksListAdapter

    private fun connectToSpotify() {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : ConnectionListener {
                override fun onFailure(p0: Throwable?) {
                    connected = false
                }

                override fun onConnected(spotifyAppRemote: SpotifyAppRemote?) {
                    buttonLayout.visibility = View.VISIBLE
                    connected = true
                    mSpotifyAppRemote = spotifyAppRemote!!
                    //TODO CHECK WHICH NUMBER mSpotifyAppRemote.playerApi.setRepeat()
                    mSpotifyAppRemote!!.playerApi.play(playlist.uri)
                    playlistTrackListAdapter.playAtIndex(0)
                    //TODO
                    mSpotifyAppRemote!!.playerApi
                        .subscribeToPlayerState()
                        .setEventCallback { s ->
                            Log.d("dupa", s.track.name)
                         }
                }

            })
    }

    private fun play() {
        mSpotifyAppRemote!!.playerApi.resume()
    }

    private fun pause() {
        mSpotifyAppRemote!!.playerApi.pause()
    }

    private fun next() {
        mSpotifyAppRemote!!.playerApi.skipNext()
    }

    private fun previous() {
        mSpotifyAppRemote!!.playerApi.skipPrevious()
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        finalSeeds = intent.getSerializableExtra("final_seeds") as Seeds

        listView = findViewById(R.id.playlistListView)

        playButton.setOnClickListener {

        }

        nextButton.setOnClickListener {
            next()
            playlistTrackListAdapter.nextTrack()
        }

        previousButton.setOnClickListener {
            previous()
            playlistTrackListAdapter.previousTrack()
        }

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
                playlistTrackListAdapter = adapter
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
                Toast.makeText(this@PlaylistActivity, "NOT DONE", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()!!
                val secondCall = apiService.createPlaylist("Bearer " + TokenService.retrieve(this@PlaylistActivity),
                    user.id!!,
                    CreatePlaylistData("Partify-" + user.display_name + " " + System.currentTimeMillis())
                )

                secondCall.enqueue(object : Callback<Playlist> {
                    override fun onFailure(call: Call<Playlist>, t: Throwable) {
                        Toast.makeText(this@PlaylistActivity, "NOT DONE", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                        playlist = response.body()!!
                        val thirdCallback = apiService.addTracksToPlaylist("Bearer " + TokenService.retrieve(this@PlaylistActivity)
                            , playlist.id!!, tracksString)
                        thirdCallback.enqueue(object : Callback<Void> {
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@PlaylistActivity, "NOT DONE", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                Toast.makeText(this@PlaylistActivity, "DONE", Toast.LENGTH_SHORT).show()
                                connectToSpotify()
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
