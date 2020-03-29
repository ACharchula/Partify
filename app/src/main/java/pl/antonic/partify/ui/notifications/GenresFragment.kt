package pl.antonic.partify.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.antonic.partify.R
import pl.antonic.partify.activities.SeedListViewSetter
import pl.antonic.partify.activities.SeedSelectionActivity

class GenresFragment : Fragment() {

    private lateinit var genresViewModel: GenresViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        genresViewModel = ViewModelProvider(this).get(GenresViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_genres, container, false)

        listView = root.findViewById(R.id.genresListView)
        (activity as SeedListViewSetter).setGenreData(listView)

        return root
    }
}
