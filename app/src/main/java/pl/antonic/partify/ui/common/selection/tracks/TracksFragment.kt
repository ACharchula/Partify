package pl.antonic.partify.ui.common.selection.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_tracks.*
import pl.antonic.partify.R
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel


class TracksFragment : Fragment() {

    private lateinit var trackRecycleViewAdapter: TrackRecycleViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_tracks, container, false)
        (activity as SeedListViewSetter).setTrackData()

        val viewModel: SeedSelectionViewModel by activityViewModels()

        if (viewModel.tracks.value == null)
            trackRecycleViewAdapter = TrackRecycleViewAdapter(ObjectList(null))
        else
            trackRecycleViewAdapter = TrackRecycleViewAdapter(viewModel.tracks.value!!)

        viewModel.tracks.observe(viewLifecycleOwner, Observer {
            trackRecycleViewAdapter.apply {
                dataSource = it
                notifyDataSetChanged()
            }
        })
        val rV = root.findViewById<RecyclerView>(R.id.trackRecycleView)

        rV.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = trackRecycleViewAdapter
        }

        return root
    }
}
