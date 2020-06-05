package pl.antonic.partify.ui.host.attributes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.antonic.partify.model.common.Attributes

class TrackAttributesViewModel : ViewModel() {
    private val _attr = MutableLiveData<Attributes>().apply {
        value = Attributes("1", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0") }
    val attr: LiveData<Attributes>
        get() = _attr

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
        val value = text.toFloat()
        _attr.value!!.popularity = (value/100).toString()
        notifyObserver()
    }

    fun changeValence(text: String) {
        val value = text.toFloat()
        _attr.value!!.valence = (value/100).toString()
        notifyObserver()
    }
}