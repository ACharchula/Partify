package pl.antonic.partify.model.spotify

import pl.antonic.partify.model.spotify.Album
import pl.antonic.partify.model.spotify.Artist

class Track {
    var album: Album? = null
    var artists: List<Artist>? = null
    var id: String? = null
    var name: String? = null
    var uri: String? = null
}