package pl.antonic.partify.activities

import android.widget.ListView

interface SeedListViewSetter {

    fun setTrackData(listView: ListView)
    fun setArtistData(listView: ListView)
    fun setGenreData(listView: ListView)

}