package pl.antonic.partify.ui.host.advertise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlinx.android.synthetic.main.activity_advertise.*
import pl.antonic.partify.R
import pl.antonic.partify.model.common.UserSelections
import pl.antonic.partify.service.common.UserService
import pl.antonic.partify.ui.host.selection.user.HostSeedSelectionActivity


class AdvertiseActivity : AppCompatActivity() {

    private lateinit var connectionsClient: ConnectionsClient
    private lateinit var viewModel: AdvertiseViewModel
    private lateinit var userRecycleViewAdapter: UserRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertise)

        viewModel = ViewModelProvider(this).get(AdvertiseViewModel::class.java)
        userRecycleViewAdapter = UserRecycleViewAdapter(viewModel.allSeeds.value!!)

        connectionsClient = Nearby.getConnectionsClient(this)

        viewModel.allSeeds.observe(this, Observer {
            userRecycleViewAdapter.apply {
                dataSource = it
                notifyDataSetChanged()
            }
        })

        usersRecycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userRecycleViewAdapter
        }

        if (!viewModel.isAdvertising)
            startAdvertising()

        if (viewModel.allSeeds.value!!.size > 0)
            advertiseLoading.visibility = View.GONE

        advertiseNextButton.setOnClickListener {
            stopAdvertising()
            val intent = Intent(this, HostSeedSelectionActivity::class.java)
            intent.putExtra("all_seeds", viewModel.getCombinedSeeds())
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        stopAdvertising()
        super.onBackPressed()
    }

    private fun stopAdvertising() {
        connectionsClient.stopAllEndpoints()
        connectionsClient.stopAdvertising()
        viewModel.isAdvertising = false
    }

    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()

        val payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {
                viewModel.updateUserSeeds(endpointId, payload)
            }

            override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
                if (update.status == PayloadTransferUpdate.Status.SUCCESS ) {
                    userRecycleViewAdapter.notifyDataSetChanged()
                }
            }
        }

        val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        userRecycleViewAdapter.notifyDataSetChanged()
                        advertiseLoading.visibility = View.GONE
                    }
                    else -> {
                        Toast.makeText(this@AdvertiseActivity, "Connection rejected", Toast.LENGTH_SHORT).show()
                        viewModel.remove(endpointId)
                        userRecycleViewAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                if (viewModel.removeIfNotCompleted(endpointId))
                    userRecycleViewAdapter.notifyDataSetChanged()
            }

            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                viewModel.addUserSelections(UserSelections(connectionInfo.endpointName, endpointId))

                AlertDialog.Builder(this@AdvertiseActivity,
                    R.style.AlertDialogCustom
                ).setTitle("Accept connection to " + connectionInfo.endpointName)
                    .setMessage("Confirm the code matches on both deices: " + connectionInfo.authenticationToken)
                    .setPositiveButton("Accept") { _, _ ->
                        connectionsClient.acceptConnection(endpointId, payloadCallback)
                    }.setNegativeButton(android.R.string.cancel) {_, _ ->
                        connectionsClient.rejectConnection(endpointId)
                    }.setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }

        connectionsClient
            .startAdvertising("Host: " + UserService.getUser().display_name!!, "pl.antonic.partify", mConnectionLifecycleCallback, advertisingOptions)
            .addOnFailureListener {
                Toast.makeText(this, "Couldn't start advertising :(", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                viewModel.isAdvertising = true
            }
    }
}
