package pl.antonic.partify.service.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import pl.antonic.partify.model.common.SelectableGenres
import pl.antonic.partify.model.spotify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyService {
    companion object {
        private const val SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1/"

        public fun getService() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private fun getApiService() : SpotifyApi {
        return getService().create(SpotifyApi::class.java)
    }

    fun getUser(accessToken: String) : MutableLiveData<User> {
        val user = MutableLiveData<User>()

        val apiService = getApiService()
        val call = apiService.getMe("Bearer $accessToken")
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Fetch error", t.message)
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                user.value = response.body()
            }
        })

        return user
    }

    fun getArtists(liveData: MutableLiveData<ObjectList<Artist>>, accessToken: String){
        val apiService = getApiService()
        val call = apiService.getTopUserArtists("Bearer $accessToken")
        call.enqueue(object : Callback<ObjectList<Artist>> {
            override fun onFailure(call: Call<ObjectList<Artist>>, t: Throwable) {
                Log.e("Fetch error", t.message)
            }
            override fun onResponse(call: Call<ObjectList<Artist>>, response: Response<ObjectList<Artist>>) {
                liveData.value = response.body()
            }
        })
    }

    fun getTracks(liveData: MutableLiveData<ObjectList<Track>>, accessToken: String) {
        val apiService = getApiService()
        val call = apiService.getTopUserTracks("Bearer $accessToken")
        call.enqueue(object : Callback<ObjectList<Track>> {
            override fun onFailure(call: Call<ObjectList<Track>>, t: Throwable) {
                Log.e("Fetch error", t.message)
            }
            override fun onResponse(call: Call<ObjectList<Track>>, response: Response<ObjectList<Track>>) {
                liveData.value = response.body()
            }
        })
    }

    fun getGenres(liveData: MutableLiveData<SelectableGenres>, accessToken: String) {
        val apiService =getApiService()
        val call = apiService.getAvailableGenres("Bearer $accessToken")
        call.enqueue(object : Callback<Genres> {
            override fun onFailure(call: Call<Genres>, t: Throwable) {
                Log.e("Fetch error", t.message)
            }
            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                val allGenres = response.body()!!
                val genreAndState = mutableListOf<SelectableGenres.Genre>()

                for (genre in allGenres.genres!!) {
                    genreAndState.add(SelectableGenres.Genre(genre))
                }
                liveData.value = SelectableGenres(genreAndState)
            }
        })
    }

    fun getTracks(
        liveData: MutableLiveData<ObjectList<Track>>,
        ids: List<String>,
        accessToken: String
    ) {
        val trackList = mutableListOf<Track>()
        val apiService = getApiService()

        for (trackId in ids) {
            val call = apiService.getTrack("Bearer $accessToken", trackId)
            call.enqueue(object : Callback<Track> {
                override fun onFailure(call: Call<Track>, t: Throwable) {
                    Log.e("Fetch error", t.message)
                }
                override fun onResponse(call: Call<Track>, response: Response<Track>) {
                    trackList.add(response.body()!!)

                    if (trackList.size == ids.size) {
                        liveData.value = ObjectList(trackList)
                    }
                }
            })
        }
    }

    fun getArtists(
        liveData: MutableLiveData<ObjectList<Artist>>,
        ids: List<String>,
        accessToken: String
    ) {
        val artistList = mutableListOf<Artist>()
        val apiService = getApiService()

        for (artistId in ids) {
            val call = apiService.getArtist("Bearer $accessToken", artistId)
            call.enqueue(object : Callback<Artist> {
                override fun onFailure(call: Call<Artist>, t: Throwable) {
                    Log.e("Fetch error", t.message)
                }
                override fun onResponse(call: Call<Artist>, response: Response<Artist>) {
                    artistList.add(response.body()!!)

                    if (artistList.size == ids.size) {
                        liveData.value = ObjectList(artistList)
                    }
                }
            })
        }
    }

}