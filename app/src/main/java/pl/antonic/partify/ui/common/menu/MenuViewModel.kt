package pl.antonic.partify.ui.common.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.antonic.partify.model.spotify.User
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.service.common.UserService
import pl.antonic.partify.service.spotify.SpotifyService

class MenuViewModel : ViewModel() {

    private val repository = SpotifyService()

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    public fun getUserData() { //TODO token storing
        if (_user.value == null) {
            repository.getUser(_user)
        }
    }

}