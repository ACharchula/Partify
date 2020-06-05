package pl.antonic.partify.ui.host.advertise

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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

        val vm: AdvertiseViewModel by viewModels()
        viewModel = vm
        userRecycleViewAdapter = UserRecycleViewAdapter(viewModel.allSeeds.value!!)

        connectionsClient = Nearby.getConnectionsClient(this)

//        viewModel.allSeeds.observe(this, Observer {
//            userRecycleViewAdapter.apply {
//                dataSource = it
//                notifyDataSetChanged()
//            }
//        }) //???

        usersRecycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userRecycleViewAdapter
        }

        if (!viewModel.isAdvertising)
            startAdvertising()

        if (viewModel.allSeeds.value!!.size > 0)
            advertiseLoading.visibility = View.GONE

        advertiseNextButton.setOnClickListener {
            connectionsClient.stopAdvertising() //stop all endpoints?

            val intent = Intent(this, HostSeedSelectionActivity::class.java)
            intent.putExtra("all_seeds", viewModel.getCombinedSeeds())
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        connectionsClient.stopAdvertising()
    }

    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        val payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {
                viewModel.updateUserSeeds(endpointId, payload)
            }

            override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
                if (update.status == PayloadTransferUpdate.Status.SUCCESS ) { //&& seedsNotNull(endpointId)) { TODO
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
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                Toast.makeText(this@AdvertiseActivity, "User disconnected", Toast.LENGTH_SHORT).show()
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
            .startAdvertising("Host: " + UserService.getUser().display_name!!, "pl.antonic.partify", mConnectionLifecycleCallback, advertisingOptions) //TODO change to username
            .addOnFailureListener {
                Toast.makeText(this, "Couldn't start advertising :(", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                viewModel.isAdvertising = true
            }
    }



//    private fun seedsNotNull(endpointId: String) : Boolean {
//        for (s in allSeeds) {
//            if (s.userName == endpointNames[endpointId]) {
//                return s.seeds != null
//            }
//        }
//        return false
//    }
}
