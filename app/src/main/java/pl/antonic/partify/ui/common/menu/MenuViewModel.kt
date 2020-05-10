package pl.antonic.partify.ui.common.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pl.antonic.partify.model.spotify.User
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.spotify.SpotifyService

class MenuViewModel : ViewModel() {

    private val repository = SpotifyService()

    private val _user by lazy {
        return@lazy repository.getUser(TokenService.getToken()) //TODO token storing
    }
    val user: LiveData<User>
        get() = _user

}