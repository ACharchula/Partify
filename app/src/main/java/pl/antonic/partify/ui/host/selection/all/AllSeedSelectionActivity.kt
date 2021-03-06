package pl.antonic.partify.ui.host.selection.all

import android.content.Intent
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.ui.common.selection.SeedSelectionActivity
import pl.antonic.partify.ui.host.attributes.TrackAttributesActivity

class AllSeedSelectionActivity : SeedSelectionActivity() {

    private lateinit var allSeeds : Seeds

    override fun getExtraData() {
        allSeeds = intent.getSerializableExtra("all_seeds") as Seeds
    }

    override fun setTrackData() {
        viewModel.getTracks(allSeeds.getList(SeedType.TRACK))
    }

    override fun setArtistData() {
        viewModel.getArtists(allSeeds.getList(SeedType.ARTIST))
    }

    override fun setGenreData() {
        viewModel.getGenres(allSeeds.getList(SeedType.GENRE))
    }

    override fun nextButtonOnClick() {
        val intent = Intent(this, TrackAttributesActivity::class.java)
        intent.putExtra("final_seeds", getSeeds())
        startActivity(intent)
    }

}
