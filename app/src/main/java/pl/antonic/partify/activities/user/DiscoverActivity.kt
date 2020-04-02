package pl.antonic.partify.activities.user

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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_discover.*
import pl.antonic.partify.NearbyClient
import pl.antonic.partify.R
import pl.antonic.partify.Seeds

class DiscoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        NearbyClient.set(Nearby.getConnectionsClient(this))

        startDiscovering()
    }

    private val REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.M)
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


    private fun startDiscovering() {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        val payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {
                val receivedBytes = payload.asBytes()
            }

            override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
                //dwdw
            }
        }

        val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        NearbyClient.get().stopDiscovery()
                        Toast.makeText(this@DiscoverActivity, "ok", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DiscoverActivity, UserSeedSelectionActivity::class.java)
                        intent.putExtra("endpointId", endpointId)
                        startActivity(intent)
                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> Toast.makeText(this@DiscoverActivity, "rejected", Toast.LENGTH_SHORT).show()
                    ConnectionsStatusCodes.STATUS_ERROR -> Toast.makeText(this@DiscoverActivity, "error", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this@DiscoverActivity, "else", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDisconnected(endpointId: String) {
                //no data can be sent
            }

            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                AlertDialog.Builder(this@DiscoverActivity,
                    R.style.AlertDialogCustom
                )
                    .setTitle("Accept connection to " + connectionInfo.endpointName)
                    .setMessage("Confirm the code matches on both deices: " + connectionInfo.authenticationToken)
                    .setPositiveButton("Accept") {_ , _ ->
                        NearbyClient.get().acceptConnection(endpointId, payloadCallback)
                    }.setNegativeButton(android.R.string.cancel) {_, _ ->
                        NearbyClient.get().rejectConnection(endpointId)
                    }.setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }

        val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                NearbyClient.get()
                    .requestConnection("nickname1", endpointId, mConnectionLifecycleCallback)
                    .addOnSuccessListener {

                    }.addOnFailureListener {

                    }
            }

            override fun onEndpointLost(endpointId: String) {
                //
            }

        }

        NearbyClient.get()
            .startDiscovery("pl.antonic.partify", endpointDiscoveryCallback, discoveryOptions)
//            .addOnSuccessListener {
//
//            }.addOnFailureListener {
//
//            }
    }
}
