package pl.antonic.partify.service.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.Seeds
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

        fun getService() : Retrofit {
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

    fun getRecommendationsInNewPlaylist(
        _tracks: MutableLiveData<ObjectList<Track>>,
        _playlist: MutableLiveData<Playlist>,
        seeds: Seeds,
        accessToken: String
    ) {
        val apiService = getApiService()

        val seedArtists: String? = nullOrJoinedElements(seeds.getList(SeedType.ARTIST))
        val seedTracks: String? = nullOrJoinedElements(seeds.getList(SeedType.TRACK))
        val seedGenres: String? = nullOrJoinedElements(seeds.getList(SeedType.GENRE))

        //TODO change market to get from user
        val call = apiService.getRecommendations(
            "Bearer $accessToken"
            , "PL", seedArtists, seedGenres, seedTracks)

        call.enqueue(object : Callback<Recommendations> {
            override fun onFailure(call: Call<Recommendations>, t: Throwable) {
                Log.e("Fetch error", t.message) // TODO error handling in every endpoint?
            }

            override fun onResponse(call: Call<Recommendations>, response: Response<Recommendations>) {
                val recommendations = response.body()!!

                //TODO check if tracks can be null
                val tracks = recommendations.tracks!!
                _tracks.value = ObjectList(tracks)
                createPlaylistAndAddTracks(tracks, _playlist, accessToken)
            }
        })
    }

    private fun createPlaylistAndAddTracks(tracks: List<Track>,
                                           _playlist: MutableLiveData<Playlist>,
                                           accessToken: String) {

        val tracksURIs = mutableListOf<String>()

        for (track in tracks) {
            tracksURIs.add(track.uri!!)
        }

        val tracksString = tracksURIs.joinToString(",")

        val apiService = getApiService()

        val call = apiService.getMe("Bearer $accessToken")
        call.enqueue(object : Callback<User> { //TODO get user id from object created at the start of the app
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Fetch error", t.message) // TODO error handling in every endpoint?
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()!!
                val secondCall = apiService.createPlaylist(
                    "Bearer $accessToken",
                    user.id!!,
                    CreatePlaylistData("Partify-" + System.currentTimeMillis())
                )

                secondCall.enqueue(object : Callback<Playlist> {
                    override fun onFailure(call: Call<Playlist>, t: Throwable) {
                        Log.e("Fetch error", t.message) // TODO error handling in every endpoint?
                    }

                    override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                        val playlist = response.body()!!
                        val thirdCallback = apiService.addTracksToPlaylist(
                            "Bearer $accessToken"
                            , playlist.id!!, tracksString)
                        thirdCallback.enqueue(object : Callback<Void> {
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("Fetch error", t.message) // TODO error handling in every endpoint?
                            }

                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                _playlist.value = playlist
                            }
                        })
                    }
                })
            }
        })
    }

    private fun nullOrJoinedElements(list: List<String>) : String? {
        return if (list.isEmpty()) null else list.joinToString(",")
    }

}