package pl.antonic.partify.service.common

import pl.antonic.partify.model.spotify.User

class UserService {
    companion object {
        private lateinit var user: User

        fun setUser(user: User) {
            this.user = user;
        }

        fun getUser(): User {
            return this.user
        }
    }
}