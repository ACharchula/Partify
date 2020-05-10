package pl.antonic.partify.service.common

import android.content.Context

class TokenService {
    companion object {
        private const val PREF_NAME = "Pref"
        private const val KEY = "user_token"
        private lateinit var token: String

        public fun store(context: Context, token: String) {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            val editor = pref.edit()
            editor.putString(KEY, token)
            editor.apply()
            this.token = token
        }

        public fun retrieve(context: Context) : String? {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            return pref.getString(KEY, null)
        }

        public fun delete(context: Context) {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            val editor = pref.edit()
            editor.remove(KEY)
            editor.apply()
        }

        public fun getToken() : String {
            return token
        }
    }
}