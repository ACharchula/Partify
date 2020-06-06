package pl.antonic.partify.ui.single

import android.content.Intent
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.ui.common.selection.SeedSelectionActivity
import pl.antonic.partify.ui.host.attributes.TrackAttributesActivity
import pl.antonic.partify.ui.user.last.UserLastActivity

class SingleUserRecommendationSelectionActivity : SeedSelectionActivity() {

    override fun nextButtonOnClick() {
        val intent = Intent(this, TrackAttributesActivity::class.java)
        intent.putExtra("final_seeds", getSeeds())
        startActivity(intent)
    }

    override fun setTrackData() {
        viewModel.getTracks()
    }

    override fun setArtistData() {
        viewModel.getArtists()
    }

    override fun setGenreData() {
        viewModel.getGenres()
    }
}
