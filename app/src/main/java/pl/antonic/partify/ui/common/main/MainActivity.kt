package pl.antonic.partify.ui.common.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_main.*
import pl.antonic.partify.R
import pl.antonic.partify.service.common.TokenService
import pl.antonic.partify.ui.common.menu.MenuActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1337
    private val CLIENT_ID = "871a79f969aa43f4923bfa59a852d6fe"
    private val REDIRECT_URI = "partify://callback"

    private val REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onStart() {
        super.onStart()
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, 1)
        }
    }

    private fun hasPermissions(
        context: Activity,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    /** Handles user acceptance (or denial) of our permission request.  */
    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 1) {
            return
        }
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "missing permission", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }
        recreate()
    }

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
                    "user-top-read",
                    "playlist-modify-private",
                    "playlist-modify-public"
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
                AuthorizationResponse.Type.ERROR -> Toast.makeText(this, "Authorization error", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Unknown error occurred!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}
