package pl.antonic.partify.ui.common.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.common.SelectableGenres
import pl.antonic.partify.model.spotify.Artist
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyService

class SeedSelectionViewModel : ViewModel() {
    private val repository = SpotifyService()

    val seeds = Seeds()

    private val _artists = MutableLiveData<ObjectList<Artist>>()
    val artists: LiveData<ObjectList<Artist>>
        get() = _artists

    private val _tracks = MutableLiveData<ObjectList<Track>>()
    val tracks: LiveData<ObjectList<Track>>
        get() = _tracks

    private val _genres = MutableLiveData<SelectableGenres>()
    val genres: LiveData<SelectableGenres>
        get() = _genres

    fun getArtists() {
        if (_artists.value == null)
            repository.getArtists(_artists, TokenService.getToken())
    }

    fun getTracks() {
        if (_tracks.value == null)
            repository.getTracks(_tracks, TokenService.getToken())
    }

    fun getGenres() {
        if (_genres.value == null)
            repository.getGenres(_genres, TokenService.getToken())
    }

    fun getArtists(ids: List<String>) {
        if (_artists.value == null)
            repository.getArtists(_artists, ids, TokenService.getToken())
    }

    fun getTracks(ids: List<String>) {
        if (_tracks.value == null)
            repository.getTracks(_tracks, ids, TokenService.getToken())
    }

    fun getGenres(ids: List<String>) {
        if (_genres.value == null) {
            val genres = mutableListOf<SelectableGenres.Genre>()
            for (id in ids) {
                genres.add(SelectableGenres.Genre(id))
            }
            _genres.value = SelectableGenres(genres)
        }
    }

    fun add(id: String, type: SeedType): Boolean {
        return seeds.add(id, type)
    }

    fun remove(id: String, type: SeedType) {
        seeds.remove(id, type)
    }
}