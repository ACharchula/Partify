package pl.antonic.partify.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.antonic.partify.R
import pl.antonic.partify.activities.SeedListViewSetter


class TracksFragment : Fragment() {

    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tracksViewModel = ViewModelProvider(this).get(TracksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tracks, container, false)

        listView = root.findViewById(R.id.trackListView)
        (activity as SeedListViewSetter).setTrackData(listView)

        return root
    }
}
