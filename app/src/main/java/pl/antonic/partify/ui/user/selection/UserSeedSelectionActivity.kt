package pl.antonic.partify.ui.user.selection

import android.content.Intent
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyApi
import pl.antonic.partify.service.spotify.SpotifyService
import pl.antonic.partify.ui.common.selection.SeedSelectionActivity
import pl.antonic.partify.ui.common.selection.tracks.TrackRecycleViewAdapter
import pl.antonic.partify.ui.user.last.UserLastActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class UserSeedSelectionActivity : SeedSelectionActivity() {

    override fun nextButtonOnClick() {
        val bytesPayload = Payload.fromBytes(Gson().toJson(getSeeds()).toByteArray())
        NearbyClientService.get().sendPayload(intent.getStringExtra("endpointId")!!, bytesPayload)

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
}
