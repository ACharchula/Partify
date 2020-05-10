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
import kotlinx.android.synthetic.main.fragment_tracks.*
import pl.antonic.partify.R
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel


class TracksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_tracks, container, false)
        (activity as SeedListViewSetter).setTrackData()

        val viewModel: SeedSelectionViewModel by activityViewModels()
        viewModel.tracks.observe(viewLifecycleOwner, Observer {
            trackRecycleView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TrackRecycleViewAdapter(it)
            }
        })

        return root
    }
}
