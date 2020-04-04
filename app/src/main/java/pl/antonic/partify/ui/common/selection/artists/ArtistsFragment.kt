package pl.antonic.partify.ui.common.selection.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.antonic.partify.R
import pl.antonic.partify.ui.common.selection.SeedListViewSetter

class ArtistsFragment : Fragment() {

    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistsViewModel =  ViewModelProvider(this).get(ArtistsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_artists, container, false)

        listView = root.findViewById(R.id.artistListView)
        (activity as SeedListViewSetter).setArtistData(listView)

        return root
    }
}
