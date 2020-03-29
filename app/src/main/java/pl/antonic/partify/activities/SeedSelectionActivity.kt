package pl.antonic.partify.activities

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_seed_selection.*
import pl.antonic.partify.R
import pl.antonic.partify.SeedType
import pl.antonic.partify.Seeds

abstract class SeedSelectionActivity : AppCompatActivity(), SeedListViewSetter, SeedListModificator {

    private val seeds = Seeds()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtraData()
        setContentView(R.layout.activity_seed_selection)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        seedSelectionNextButton.setOnClickListener {
            nextButtonOnClick()
        }
    }

    override fun addId(id: String, type: SeedType) : Boolean {
        if (!seeds.add(id, type)) {
            Toast.makeText(this, "MAX", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun removeId(id: String, type: SeedType) {
        seeds.remove(id, type)
    }

    //TODO replace with new variable
    override fun containsId(id: String, type: SeedType): Boolean {
        return seeds.contains(id, type)
    }

    public abstract fun nextButtonOnClick()

    protected fun getSeeds() : Seeds {
        return seeds
    }

    open fun getExtraData() {}
}
