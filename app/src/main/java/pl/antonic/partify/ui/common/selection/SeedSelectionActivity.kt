package pl.antonic.partify.ui.common.selection

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_advertise.*
import kotlinx.android.synthetic.main.activity_seed_selection.*
import kotlinx.android.synthetic.main.fragment_artists.*
import kotlinx.android.synthetic.main.fragment_genres.*
import kotlinx.android.synthetic.main.fragment_tracks.*
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.ui.common.selection.artists.ArtistRecycleViewAdapter
import pl.antonic.partify.ui.common.selection.genres.GenreRecycleViewAdapter
import pl.antonic.partify.ui.common.selection.tracks.TrackRecycleViewAdapter

abstract class SeedSelectionActivity : AppCompatActivity(),
    SeedListViewSetter,
    SeedListModificator {

    lateinit var viewModel: SeedSelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtraData()
        setContentView(R.layout.activity_seed_selection)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this).get(SeedSelectionViewModel::class.java)

        seedSelectionNextButton.setOnClickListener {
            if (selectedAtLeastOne()) {
                nextButtonOnClick()
            } else {
                Toast.makeText(this, "Please select at least one seed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectedAtLeastOne() : Boolean {
        return getSeeds().currentSize() != 0
    }

    override fun addId(id: String, type: SeedType) : Boolean {
        if (!viewModel.add(id,type)) {
            Toast.makeText(this, "Maximal amount of fields already selected!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun removeId(id: String, type: SeedType) {
        viewModel.remove(id, type)
    }

    abstract fun nextButtonOnClick()

    protected fun getSeeds() : Seeds {
        return viewModel.seeds
    }

    open fun getExtraData() {}
}
