package pl.antonic.partify.ui.common.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.spotify.sdk.android.auth.AuthorizationClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import pl.antonic.partify.ui.user.discover.DiscoverActivity
import pl.antonic.partify.R
import pl.antonic.partify.ui.host.advertise.AdvertiseActivity
import pl.antonic.partify.service.common.UserService
import pl.antonic.partify.ui.common.main.MainActivity
import pl.antonic.partify.ui.single.SingleUserRecommendationSelectionActivity

class MenuActivity : AppCompatActivity() {

    //TODO Market query?

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

        logoutButton.setOnClickListener {
            AuthorizationClient.clearCookies(this)
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }

        singleRecommendationButton.setOnClickListener {
            val intent = Intent(this, SingleUserRecommendationSelectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfile() {
        val model : MenuViewModel by viewModels()
        model.getUserData()
        model.user.observe(this, Observer {
            UserService.setUser(it)
            if (it.images != null)
                Picasso.get().load(it.images!![0].url).into(profilePictureImageView)
            if (it.display_name != null) {
                displayNameTextView.text = it.display_name
            } else {
                displayNameTextView.text = "Couldn't load name data"
            }
        })
    }
}
