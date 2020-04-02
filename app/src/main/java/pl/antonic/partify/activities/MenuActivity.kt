package pl.antonic.partify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import pl.antonic.partify.activities.user.DiscoverActivity
import pl.antonic.partify.R
import pl.antonic.partify.activities.host.AdvertiseActivity
import pl.antonic.partify.activities.host.HostSeedSelectionActivity
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    //TODO Market query ?!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadProfile()
        setContentView(R.layout.activity_menu)

        hostPartyButton.setOnClickListener {
            val intent = Intent(this, AdvertiseActivity::class.java)
            startActivity(intent)
        }

        joinPartyButton.setOnClickListener {
            val intent = Intent(this, DiscoverActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfile() {
        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getMe("Bearer " + TokenService.retrieve(this))
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                Picasso.get().load(response.body()!!.images!![0].url).into(profilePictureImageView)
                displayNameTextView.text = response.body()!!.display_name
            }

        })
    }
}
