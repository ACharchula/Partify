package pl.antonic.partify.model.spotify

import java.io.Serializable

open class Artist : Serializable {
    var id: String? = null
    var images: List<Image>? = null
    var name: String? = null

    var selected = false
}