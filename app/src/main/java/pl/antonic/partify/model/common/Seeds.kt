package pl.antonic.partify.model.common

import java.io.Serializable

class Seeds : Serializable {

    private val MAX_AMOUNT = 5

    private var artistsIds = mutableListOf<String>()
    private var tracksIds = mutableListOf<String>()
    private var genresIds = mutableListOf<String>()

    public fun add(id: String, type: SeedType) : Boolean {
        if (exceedsMaxAmount())
            return false

        when(type) {
            SeedType.ARTIST -> artistsIds.add(id)
            SeedType.TRACK -> tracksIds.add(id)
            SeedType.GENRE -> genresIds.add(id)
        }

        return true
    }

    public fun remove(id: String, type: SeedType) {
        when(type) {
            SeedType.ARTIST -> artistsIds.remove(id)
            SeedType.TRACK -> tracksIds.remove(id)
            SeedType.GENRE -> genresIds.remove(id)
        }
    }

    public fun combineSeeds(seeds: Seeds) : Seeds {
        val combined = Seeds()

        combined.artistsIds.addAll(artistsIds)
        combined.artistsIds.addAll(seeds.artistsIds)

        combined.tracksIds.addAll(tracksIds)
        combined.tracksIds.addAll(seeds.tracksIds)

        combined.genresIds.addAll(genresIds)
        combined.genresIds.addAll(seeds.genresIds)

        return combined
    }

    private fun exceedsMaxAmount() : Boolean {
        return artistsIds.size + tracksIds.size + genresIds.size >= MAX_AMOUNT
    }

    public fun getList(type: SeedType) : List<String> {
        return when (type) {
            SeedType.ARTIST -> artistsIds.toList()
            SeedType.TRACK -> tracksIds.toList()
            SeedType.GENRE -> genresIds.toList()
        }
    }

    fun contains(id: String, type: SeedType) : Boolean {
        return when (type) {
            SeedType.ARTIST -> artistsIds.contains(id)
            SeedType.TRACK -> tracksIds.contains(id)
            SeedType.GENRE -> genresIds.contains(id)
        }
    }
}