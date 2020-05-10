package pl.antonic.partify.ui.common.selection.artists

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
import kotlinx.android.synthetic.main.fragment_artists.*
import pl.antonic.partify.R
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel

class ArtistsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_artists, container, false)
        (activity as SeedListViewSetter).setArtistData()

        val viewModel: SeedSelectionViewModel by activityViewModels()

        viewModel.artists.observe(viewLifecycleOwner, Observer {
            artistRecycleView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ArtistRecycleViewAdapter(it)
            }
        })

        return root
    }
}
