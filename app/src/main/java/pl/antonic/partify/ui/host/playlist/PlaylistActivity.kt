package pl.antonic.partify.ui.host.playlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_playlist.*
import pl.antonic.partify.R
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.spotify.*
import pl.antonic.partify.service.spotify.SpotifyService
import pl.antonic.partify.ui.common.menu.MenuActivity
import java.util.*
import java.util.concurrent.TimeUnit

class PlaylistActivity : AppCompatActivity(), PlaylistTrackSelector{

    private lateinit var finalSeeds : Seeds

    val CLIENT_ID = "871a79f969aa43f4923bfa59a852d6fe"
    val REDIRECT_URI = "partify://callback"

    private lateinit var viewModel: PlaylistViewModel
    private lateinit var tracksRecycleViewAdapter : PlaylistTracksRecycleViewAdapter
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    override fun onResume() {
        super.onResume()
        connectToSpotifyAppRemote()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        finalSeeds = intent.getSerializableExtra("final_seeds") as Seeds
        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)

        tracksRecycleViewAdapter = if (viewModel.tracks.value != null)
            PlaylistTracksRecycleViewAdapter(viewModel.tracks.value!!)
        else
            PlaylistTracksRecycleViewAdapter(ObjectList(null))

        viewModel.tracks.observe(this, Observer {
            tracksRecycleViewAdapter.apply {
                dataSource = it
                notifyDataSetChanged()
            }
        })

        viewModel.playerState.observe(this, Observer {
            val playerState = it

            val allArtists = mutableListOf<String>()
            for (artist in playerState.track.artists) {
                allArtists.add(artist.name!!)
            }
            playlistArtist.text = allArtists.joinToString()
            playlistTrack.text = playerState.track.name //TODO add album

            tracksRecycleViewAdapter.apply {
                currentlyPlayingUri = playerState.track.uri
                notifyDataSetChanged()
            }

            if (playerState.isPaused) {
                playButton.setImageResource(R.drawable.ic_play_arrow_white_48dp)
            } else {
                playButton.setImageResource(R.drawable.ic_pause_white_48dp)
            }

            playlistSeekBar.max = playerState.track.duration.toInt()
            playlistSeekBar.progress = playerState.playbackPosition.toInt()

            duration.text = transformToMinutesAndSeconds(playerState.track.duration)
            playbackPosition.text = transformToMinutesAndSeconds(playerState.playbackPosition)
        })

        playlistListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tracksRecycleViewAdapter
        }

        viewModel.playlist.observe(this, Observer {
            if (!viewModel.hasPlaylistStarted) {
                mSpotifyAppRemote!!.playerApi.play(viewModel.playlist.value!!.uri)
                buttonLayout.visibility = View.VISIBLE
                viewModel.hasPlaylistStarted = true
            }
        })

        viewModel.getTracks(finalSeeds)

        nextButton.setOnClickListener {
            next()
        }

        previousButton.setOnClickListener {
            previous()
        }

        playButton.setOnClickListener {
            val isPaused = viewModel.playerState.value?.isPaused ?: false
            if (isPaused) {
                play()
            } else {
                pause()
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val isPaused = viewModel.playerState.value?.isPaused ?: true
                if (!isPaused) {
                    playlistSeekBar.progress += 10
                }
            }

        } , 0, 10)

        playlistSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                playbackPosition.text = transformToMinutesAndSeconds(p1.toLong())
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                mSpotifyAppRemote!!.playerApi.seekTo(p0!!.progress.toLong())
            }

        })

        finalPlaylistCloseButton.setOnClickListener {
            AlertDialog.Builder(this@PlaylistActivity,
                R.style.AlertDialogCustom
            ).setTitle("Closing")
                .setMessage("Do you want to keep the playlist?")
                .setPositiveButton("Yes") { _, _ ->
                    goToMenu()
                }.setNegativeButton("No") {_, _ ->
                    viewModel.deletePlaylist()
                    goToMenu()
                }.setIcon(android.R.drawable.ic_menu_close_clear_cancel).show()
        }
    }

    private fun goToMenu() {
        mSpotifyAppRemote!!.playerApi.pause()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun playSelectedTrack(position: Int) {
        mSpotifyAppRemote!!.playerApi.skipToIndex(viewModel.playlist.value!!.uri, position)
    }

    private fun connectToSpotifyAppRemote() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : ConnectionListener {
                override fun onFailure(p0: Throwable?) {
                    Toast.makeText(this@PlaylistActivity, "Couldn't connect to Spotify app remote!", Toast.LENGTH_SHORT).show()
                }

                override fun onConnected(spotifyAppRemote: SpotifyAppRemote?) {
                    mSpotifyAppRemote = spotifyAppRemote
                    //TODO CHECK WHICH NUMBER mSpotifyAppRemote.playerApi.setRepeat() if it is in loop
                    subscribeToPlayerState()
                }
            })
    }

    private fun subscribeToPlayerState() {
        mSpotifyAppRemote!!.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState ->
                viewModel.setPlayerState(playerState)
            }
    }

    private fun transformToMinutesAndSeconds(millis: Long) : String {
        return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
    }

    private fun play() {
        mSpotifyAppRemote!!.playerApi.resume()
    }

    private fun pause() {
        mSpotifyAppRemote!!.playerApi.pause()
    }

    private fun next() {
        mSpotifyAppRemote!!.playerApi!!.skipNext()
    }

    private fun previous() {
        mSpotifyAppRemote!!.playerApi!!.skipPrevious()
    }
}
