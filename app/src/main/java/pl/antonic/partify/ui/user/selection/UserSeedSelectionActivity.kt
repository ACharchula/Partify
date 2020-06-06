package pl.antonic.partify.ui.user.selection

import android.content.Intent
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.ui.common.selection.SeedSelectionActivity
import pl.antonic.partify.ui.user.last.UserLastActivity

open class UserSeedSelectionActivity : SeedSelectionActivity() {

    override fun nextButtonOnClick() {
        val bytesPayload = Payload.fromBytes(Gson().toJson(getSeeds()).toByteArray())
        NearbyClientService.send(bytesPayload)
        val intent = Intent(this, UserLastActivity::class.java)
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

    override fun onBackPressed() {}
}
