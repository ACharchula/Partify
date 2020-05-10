package pl.antonic.partify.ui.common.selection.genres

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
import kotlinx.android.synthetic.main.fragment_genres.*
import pl.antonic.partify.R
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel

class GenresFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_genres, container, false)
        (activity as SeedListViewSetter).setGenreData()

        val viewModel: SeedSelectionViewModel by activityViewModels()
        viewModel.genres.observe(viewLifecycleOwner, Observer {
            genresRecycleView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = GenreRecycleViewAdapter(it)
            }
        })

        return root
    }
}
