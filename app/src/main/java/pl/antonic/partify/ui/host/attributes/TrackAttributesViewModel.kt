package pl.antonic.partify.ui.host.attributes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.antonic.partify.model.common.Attributes
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.spotify.Playlist
import pl.antonic.partify.service.spotify.SpotifyService

class TrackAttributesViewModel : ViewModel() {
    private val repository = SpotifyService()

    private val _attr = MutableLiveData<Attributes>().apply {
        value = Attributes("1", "0.0", "0.0", "0.0", "0.0", "0", "0.0") }
    val attr: LiveData<Attributes>
        get() = _attr

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist>
        get() = _playlist

    private fun notifyObserver() {
        _attr.value = _attr.value
    }

    fun changeLimit(text: String) {
        val value = text.toInt()
        _attr.value!!.limit = (value+1).toString() // limit must be greater than 0
        notifyObserver()
    }

    fun changeAcousticness(text: String) {
        val value = text.toFloat()
        _attr.value!!.acousticness = (value/100).toString()
        notifyObserver()
    }

    fun changeDanceability(text: String) {
        val value = text.toFloat()
        _attr.value!!.danceability = (value/100).toString()
        notifyObserver()
    }

    fun changeEnergy(text: String) {
        val value = text.toFloat()
        _attr.value!!.energy = (value/100).toString()
        notifyObserver()
    }

    fun changeInstrumentalness(text: String) {
        val value = text.toFloat()
        _attr.value!!.instrumentalness = (value/100).toString()
        notifyObserver()
    }

    fun changePopularity(text: String) {
        _attr.value!!.popularity = text
        notifyObserver()
    }

    fun changeValence(text: String) {
        val value = text.toFloat()
        _attr.value!!.valence = (value/100).toString()
        notifyObserver()
    }

    fun getRecommendations(seeds: Seeds, attr: Attributes) {
        repository.getRecommendationsInNewPlaylist(_playlist, seeds, attr)
    }
}