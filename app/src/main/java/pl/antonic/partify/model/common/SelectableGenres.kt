package pl.antonic.partify.model.common

class SelectableGenres(var genres: List<Genre>? = null) {
    class Genre(var name: String, var selected: Boolean = false)
}