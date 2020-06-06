package pl.antonic.partify.ui.host.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotify.protocol.types.PlayerState
import pl.antonic.partify.model.spotify.Playlist
import pl.antonic.partify.service.spotify.SpotifyService

class PlaylistViewModel : ViewModel() {

    private val repository = SpotifyService()

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState>
        get() = _playerState

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist>
        get() = _playlist

    var hasPlaylistStarted: Boolean = false

    fun setPlayerState(playerState: PlayerState) {
        _playerState.value = playerState
    }

    fun deletePlaylist() {
        repository.deletePlaylist(_playlist.value!!.id)
    }

    fun setIfNull(playlist: Playlist) {
        if (_playlist.value == null) {
            _playlist.value = playlist
        }
    }
}