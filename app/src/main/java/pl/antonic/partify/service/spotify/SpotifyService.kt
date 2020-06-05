package pl.antonic.partify.service.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.common.SelectableGenres
import pl.antonic.partify.model.spotify.*
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.common.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyService {
    companion object {
        private const val SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1/"
        private var service: Retrofit? = null

        fun getService() : Retrofit {
            if (service != null) {
                return service!!
            } else {
                val httpClient = OkHttpClient.Builder()

                httpClient.addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + TokenService.getToken())
                        .build()
                    chain.proceed(request)
                }

                return Retrofit.Builder()
                    .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
        }
    }

    private fun getApiService() : SpotifyApi {
        return getService().create(SpotifyApi::class.java)
    }

    fun getUser(user: MutableLiveData<User>) : MutableLiveData<User> {

        val apiService = getApiService()
        val call = apiService.getMe()
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                user.value = response.body()
            }
        })

        return user
    }

    fun getArtists(liveData: MutableLiveData<ObjectList<Artist>>){
        val apiService = getApiService()
        val call = apiService.getTopUserArtists()
        call.enqueue(object : Callback<ObjectList<Artist>> {
            override fun onFailure(call: Call<ObjectList<Artist>>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }
            override fun onResponse(call: Call<ObjectList<Artist>>, response: Response<ObjectList<Artist>>) {
                liveData.value = response.body()
            }
        })
    }

    fun getTracks(liveData: MutableLiveData<ObjectList<Track>>) {
        val apiService = getApiService()
        val call = apiService.getTopUserTracks()
        call.enqueue(object : Callback<ObjectList<Track>> {
            override fun onFailure(call: Call<ObjectList<Track>>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }
            override fun onResponse(call: Call<ObjectList<Track>>, response: Response<ObjectList<Track>>) {
                liveData.value = response.body()
            }
        })
    }

    fun getGenres(liveData: MutableLiveData<SelectableGenres>) {
        val apiService =getApiService()
        val call = apiService.getAvailableGenres()
        call.enqueue(object : Callback<Genres> {
            override fun onFailure(call: Call<Genres>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
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
        ids: List<String>
    ) {
        val trackList = mutableListOf<Track>()
        val apiService = getApiService()

        for (trackId in ids) {
            val call = apiService.getTrack(trackId)
            call.enqueue(object : Callback<Track> {
                override fun onFailure(call: Call<Track>, t: Throwable) {
                    Log.e("Fetch error", t.message!!)
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
        ids: List<String>
    ) {
        val artistList = mutableListOf<Artist>()
        val apiService = getApiService()

        for (artistId in ids) {
            val call = apiService.getArtist(artistId)
            call.enqueue(object : Callback<Artist> {
                override fun onFailure(call: Call<Artist>, t: Throwable) {
                    Log.e("Fetch error", t.message!!)
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

    fun getRecommendationsInNewPlaylist(
        _tracks: MutableLiveData<ObjectList<Track>>,
        _playlist: MutableLiveData<Playlist>,
        seeds: Seeds
    ) {
        val apiService = getApiService()

        val seedArtists: String? = nullOrJoinedElements(seeds.getList(SeedType.ARTIST))
        val seedTracks: String? = nullOrJoinedElements(seeds.getList(SeedType.TRACK))
        val seedGenres: String? = nullOrJoinedElements(seeds.getList(SeedType.GENRE))

        val call = apiService.getRecommendations(UserService.getUser().country!!, seedArtists, seedGenres, seedTracks)

        call.enqueue(object : Callback<Recommendations> {
            override fun onFailure(call: Call<Recommendations>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }

            override fun onResponse(call: Call<Recommendations>, response: Response<Recommendations>) {
                val recommendations = response.body()!!

                val tracks = recommendations.tracks!!
                _tracks.value = ObjectList(tracks)
                createPlaylistAndAddTracks(tracks, _playlist)
            }
        })
    }

    private fun createPlaylistAndAddTracks(tracks: List<Track>,
                                           _playlist: MutableLiveData<Playlist>) {

        val tracksURIs = mutableListOf<String>()

        for (track in tracks) {
            tracksURIs.add(track.uri!!)
        }

        val tracksString = tracksURIs.joinToString(",")

        val apiService = getApiService()

        val call = apiService.createPlaylist(
            UserService.getUser().id!!,
            CreatePlaylistData("Partify-" + System.currentTimeMillis())
        )

        call.enqueue(object : Callback<Playlist> {
            override fun onFailure(call: Call<Playlist>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }

            override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                val playlist = response.body()!!
                val thirdCallback = apiService.addTracksToPlaylist(playlist.id!!, tracksString)
                thirdCallback.enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("Fetch error", t.message!!)
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        _playlist.value = playlist
                    }
                })
            }
        })
    }

    private fun nullOrJoinedElements(list: List<String>) : String? {
        return if (list.isEmpty()) null else list.joinToString(",")
    }

    fun deletePlaylist(id: String?) {
        val apiService = getApiService()
        val call = apiService.unfollowPlaylist(id!!)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Fetch error", t.message!!)
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }

}