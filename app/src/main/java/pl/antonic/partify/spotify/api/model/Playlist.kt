package pl.antonic.partify.spotify.api.model

class Playlist {
    var uri: String? = null
    var tracks: ObjectList<Track>? = null
    var id: String? = null
}