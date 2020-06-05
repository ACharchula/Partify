package pl.antonic.partify.model.spotify

import java.io.Serializable

class Playlist : Serializable {
    var uri: String? = null
    var tracks: ObjectList<Track>? = null
    var id: String? = null
}