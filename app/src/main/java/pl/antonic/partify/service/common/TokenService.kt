package pl.antonic.partify.service.common

class TokenService {
    companion object {
        private lateinit var token: String

        fun save(token: String) {
            this.token = token
        }

        fun getToken() : String {
            return token
        }
    }
}