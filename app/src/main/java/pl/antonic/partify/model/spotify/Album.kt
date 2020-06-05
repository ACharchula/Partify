package pl.antonic.partify.model.spotify

import java.io.Serializable

class Album : Serializable{
    var artist: Artist? = null
    var id: String? = null
    var images: List<Image>? = null
    var name: String? = null

}