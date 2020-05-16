package pl.antonic.partify.ui.host.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotify.protocol.types.PlayerState
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Playlist
import pl.antonic.partify.model.spotify.Track
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyService

class PlaylistViewModel : ViewModel() {

    private val repository = SpotifyService()

    private val _tracks = MutableLiveData<ObjectList<Track>>()
    val tracks: LiveData<ObjectList<Track>>
        get() = _tracks

    private val _currentlyPlaying = MutableLiveData<Int>().apply { value = -1 }
    val currentlyPlaying: LiveData<Int>
        get() = _currentlyPlaying

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState>
        get() = _playerState

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist>
        get() = _playlist

    var isPlaylistStarted: Boolean = false

    fun selectAtIndex(i: Int) {
        _currentlyPlaying.value = i
    }

    fun getTracks(seeds: Seeds) {
        if (_tracks.value == null)
            repository.getRecommendationsInNewPlaylist(_tracks, _playlist, seeds, TokenService.getToken())
    }

    fun setPlayerState(playerState: PlayerState) {
        _playerState.value = playerState
    }

    fun nextTrack()  {
        if (_currentlyPlaying.value == _tracks.value!!.items!!.size - 1) {
            _currentlyPlaying.value = 0
        } else {
            _currentlyPlaying.value = _currentlyPlaying.value?.plus(1)
        }
    }

    fun previousTrack() {
        if (_currentlyPlaying.value == 0) {
            _currentlyPlaying.value = _tracks.value!!.items!!.size - 1
        } else {
            _currentlyPlaying.value = _currentlyPlaying.value?.minus(1)
        }
    }

    fun getCurrentTrack(): Track {
        return _tracks.value!!.items!![_currentlyPlaying.value!!]
    }

    fun setIndexBasedOnName(name: String?) {
        var i = 0
        for (track in _tracks.value!!.items!!) {
            if (track.name == name) {
                _currentlyPlaying.value = i
            } else {
                i++
            }
        }
    }
}