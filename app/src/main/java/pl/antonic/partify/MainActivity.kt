package pl.antonic.partify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_main.*
import pl.antonic.partify.service.TokenService

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 1337
    val CLIENT_ID = "871a79f969aa43f4923bfa59a852d6fe"
    val REDIRECT_URI = "partify://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton.setOnClickListener {
            val builder = AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
            )
            builder.setScopes(
                arrayOf(
                    "streaming",
                    "user-read-recently-played",
                    "user-top-read"
                )
            )

            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN ->  {
                    TokenService.store(this, response.accessToken)
                    goToMenu()
                }
                AuthorizationResponse.Type.ERROR -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Unknown error occurred!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        AuthorizationClient.clearCookies(this)
        TokenService.delete(this)
    }
}
