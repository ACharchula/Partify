package pl.antonic.partify.ui.common.selection

import android.widget.ListView

interface SeedListViewSetter {

    fun setTrackData(listView: ListView)
    fun setArtistData(listView: ListView)
    fun setGenreData(listView: ListView)

}