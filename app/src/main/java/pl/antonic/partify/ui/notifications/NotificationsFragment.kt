package pl.antonic.partify.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.antonic.partify.GenreListAdapter
import pl.antonic.partify.R
import pl.antonic.partify.TrackListAdapter
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.Genres
import pl.antonic.partify.spotify.api.model.ObjectList
import pl.antonic.partify.spotify.api.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        listView = root.findViewById(R.id.genresListView)

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getAvailableGenres("Bearer " + TokenService.retrieve(activity!!.applicationContext))
        call.enqueue(object : Callback<Genres> {
            override fun onFailure(call: Call<Genres>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                val result = response.body()
                val adapter = GenreListAdapter(activity!!.applicationContext, result!!)
                listView.adapter = adapter
            }

        })

        return root
    }
}
