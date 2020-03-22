package pl.antonic.partify.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_dashboard.*
import pl.antonic.partify.R
import pl.antonic.partify.TrackListAdapter
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.ObjectList
import pl.antonic.partify.spotify.api.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        listView = root.findViewById(R.id.trackListView)

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getTopUserTracks("Bearer " + TokenService.retrieve(activity!!.applicationContext))
        call.enqueue(object : Callback<ObjectList<Track>> {
            override fun onFailure(call: Call<ObjectList<Track>>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<ObjectList<Track>>, response: Response<ObjectList<Track>>) {
                val result = response.body()
                val adapter = TrackListAdapter(activity!!.applicationContext, result!!)
                listView.adapter = adapter
            }

        })

        return root
    }
}
