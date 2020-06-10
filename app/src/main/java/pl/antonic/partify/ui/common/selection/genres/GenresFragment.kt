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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_genres.*
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SelectableGenres
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.ui.common.selection.SeedListViewSetter
import pl.antonic.partify.ui.common.selection.SeedSelectionViewModel

class GenresFragment : Fragment() {

    private lateinit var genresRecycleViewAdapter: GenreRecycleViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_genres, container, false)
        (activity as SeedListViewSetter).setGenreData()

        val viewModel: SeedSelectionViewModel by activityViewModels()

        if (viewModel.genres.value == null)
            genresRecycleViewAdapter = GenreRecycleViewAdapter(SelectableGenres(null))
        else
            genresRecycleViewAdapter = GenreRecycleViewAdapter(viewModel.genres.value!!)

        viewModel.genres.observe(viewLifecycleOwner, Observer {
            genresRecycleViewAdapter.apply {
                dataSource = it
                notifyDataSetChanged()
            }
        })

        val rV = root.findViewById<RecyclerView>(R.id.genresRecycleView)

        rV.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = genresRecycleViewAdapter
        }

        return root
    }
}
