package pl.antonic.partify

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.Artist
import pl.antonic.partify.spotify.api.model.ObjectList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

//        val retrofit = SpotifyService.getService()
//        val apiService = retrofit.create(SpotifyApi::class.java)
//        val call = apiService.getTopUserArtists("Bearer " + TokenService.retrieve(this))
//        call.enqueue(object : Callback<ObjectList<Artist>> {
//            override fun onFailure(call: Call<ObjectList<Artist>>, t: Throwable) {
//                val a = 1
//            }
//
//            override fun onResponse(call: Call<ObjectList<Artist>>, response: Response<ObjectList<Artist>>) {
//                val b = 2
//            }
//
//        })
    }
}
