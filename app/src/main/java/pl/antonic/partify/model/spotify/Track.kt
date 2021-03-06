package pl.antonic.partify.model.spotify

import java.io.Serializable

class Track : Serializable {
    var album: Album? = null
    var artists: List<Artist>? = null
    var id: String? = null
    var name: String? = null
    var uri: String? = null
    var duration_ms: Long? = null

    var selected = false
}