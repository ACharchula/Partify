package pl.antonic.partify.ui.user.discover

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.R
import pl.antonic.partify.service.common.UserService
import pl.antonic.partify.ui.user.selection.UserSeedSelectionActivity

class DiscoverActivity : AppCompatActivity() {

    private lateinit var viewModel: DiscoverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        NearbyClientService.set(Nearby.getConnectionsClient(this))

        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)

        if (!viewModel.isDiscovering)
            startDiscovering()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        NearbyClientService.get().stopDiscovery()
        viewModel.isDiscovering = false
    }

    private fun startDiscovering() {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        val payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {}
            override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
        }

        val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        NearbyClientService.get().stopDiscovery()
                        val intent = Intent(this@DiscoverActivity, UserSeedSelectionActivity::class.java)
                        intent.putExtra("endpointId", endpointId)
                        startActivity(intent)
                    }
                    else -> Toast.makeText(this@DiscoverActivity, "Couldn't connect to host", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDisconnected(endpointId: String) {
                Toast.makeText(this@DiscoverActivity, "Host disconnected - please try again", Toast.LENGTH_SHORT).show()
                NearbyClientService.get().stopDiscovery()
                onBackPressed()
            }

            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                AlertDialog.Builder(this@DiscoverActivity,
                    R.style.AlertDialogCustom
                )
                    .setTitle("Accept connection to " + connectionInfo.endpointName)
                    .setMessage("Confirm the code matches on both deices: " + connectionInfo.authenticationToken)
                    .setPositiveButton("Accept") {_ , _ ->
                        NearbyClientService.get().acceptConnection(endpointId, payloadCallback)
                    }.setNegativeButton(android.R.string.cancel) {_, _ ->
                        NearbyClientService.get().rejectConnection(endpointId)
                    }.setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }

        val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                NearbyClientService.get().requestConnection(UserService.getUser().display_name!!, endpointId, mConnectionLifecycleCallback)
            }
            override fun onEndpointLost(endpointId: String) {}
        }

        NearbyClientService.get()
            .startDiscovery("pl.antonic.partify", endpointDiscoveryCallback, discoveryOptions)
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                viewModel.isDiscovering = true
            }
    }
}
