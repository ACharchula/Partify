package pl.antonic.partify.ui.common.selection.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_artists.*
import pl.antonic.partify.R
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel

class ArtistsFragment : Fragment() {

    private lateinit var artistRecycleViewAdapter: ArtistRecycleViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_artists, container, false)
        (activity as SeedListViewSetter).setArtistData()

        val viewModel: SeedSelectionViewModel by activityViewModels()

        if (viewModel.artists.value == null)
            artistRecycleViewAdapter = ArtistRecycleViewAdapter(ObjectList(null))
        else
            artistRecycleViewAdapter = ArtistRecycleViewAdapter(viewModel.artists.value!!)

        viewModel.artists.observe(viewLifecycleOwner, Observer {
            artistRecycleViewAdapter.apply {
                dataSource = it
                notifyDataSetChanged()
            }
        })

        val rV = root.findViewById<RecyclerView>(R.id.artistRecycleView)
        rV.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = artistRecycleViewAdapter
        }

        return root
    }
}
