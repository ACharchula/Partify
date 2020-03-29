package pl.antonic.partify.spotify.api.model

class Track {
    var album: Album? = null
    var artists: List<Artist>? = null
    var id: String? = null
    var name: String? = null
    var uri: String? = null
}