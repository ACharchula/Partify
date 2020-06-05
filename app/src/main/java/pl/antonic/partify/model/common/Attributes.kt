package pl.antonic.partify.model.common

import java.io.Serializable

class Attributes(
    var limit: String?,
    var acousticness: String?,
    var danceability: String?,
    var energy: String?,
    var instrumentalness: String?,
    var popularity: String?,
    var valence: String?
) : Serializable {

}