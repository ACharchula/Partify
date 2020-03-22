package pl.antonic.partify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.antonic.partify.ArtistListAdapter
import pl.antonic.partify.R
import pl.antonic.partify.service.TokenService
import pl.antonic.partify.spotify.api.SpotifyApi
import pl.antonic.partify.spotify.api.SpotifyService
import pl.antonic.partify.spotify.api.model.Artist
import pl.antonic.partify.spotify.api.model.ObjectList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =  ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        listView = root.findViewById(R.id.artistListView)

        val retrofit = SpotifyService.getService()
        val apiService = retrofit.create(SpotifyApi::class.java)
        val call = apiService.getTopUserArtists("Bearer " + TokenService.retrieve(activity!!.applicationContext))
        call.enqueue(object : Callback<ObjectList<Artist>> {
            override fun onFailure(call: Call<ObjectList<Artist>>, t: Throwable) {
                val a = 1
            }

            override fun onResponse(call: Call<ObjectList<Artist>>, response: Response<ObjectList<Artist>>) {
                val result = response.body()
                val adapter = ArtistListAdapter(activity!!.applicationContext, result!!)
                listView.adapter = adapter
            }

        })
        return root
    }
}
