package pl.antonic.partify.ui.common.selection

import pl.antonic.partify.model.common.SeedType

interface SeedListModificator {

    fun addId(id: String, type: SeedType) : Boolean
    fun removeId(id: String, type: SeedType)
    fun containsId(id: String, type: SeedType) : Boolean
}