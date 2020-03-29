package pl.antonic.partify.activities

import pl.antonic.partify.SeedType

interface SeedListModificator {

    fun addId(id: String, type: SeedType) : Boolean
    fun removeId(id: String, type: SeedType)
    fun containsId(id: String, type: SeedType) : Boolean
}