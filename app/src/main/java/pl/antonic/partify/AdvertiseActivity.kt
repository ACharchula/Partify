package pl.antonic.partify

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_advertise.*


class AdvertiseActivity : AppCompatActivity() {

    private lateinit var connectionsClient: ConnectionsClient
    private lateinit var listView: ListView
    private lateinit var userListAdapter: UserListAdapter

    private val allSeeds = mutableListOf<UserSelections>()
    private val endpointNames = hashMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertise)
        listView = findViewById(R.id.usersListView)

        connectionsClient = Nearby.getConnectionsClient(this)

        val adapter = UserListAdapter(this, allSeeds)
        listView.adapter = adapter
        userListAdapter = adapter

        startAdvertising()
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

    private fun seedsNotNull(endpointId: String) : Boolean {
        for (s in allSeeds) {
            if (s.userName == endpointNames[endpointId]) {
                return s.seeds != null
            }
        }
        return false
    }

    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        val payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {
                if (payload.asBytes() != null) {
                    val seeds = Gson().fromJson(String(payload.asBytes()!!), Seeds::class.java)

                    for (s in allSeeds) {
                        if (s.userName == endpointNames[endpointId]) {
                            s.seeds = seeds
                            break
                        }
                    }
                }
            }

            override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
                if (update.status == PayloadTransferUpdate.Status.SUCCESS && seedsNotNull(endpointId)) {
                    userListAdapter.notifyDataSetChanged()
                }
            }
        }

        val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        val userName = if (endpointNames[endpointId] != null) endpointNames[endpointId]!! else endpointId
                        allSeeds.add(UserSelections(userName))
                        userListAdapter.notifyDataSetChanged()
                        advertiseLoading.visibility = View.GONE
                    }
                    else -> {
                        Toast.makeText(this@AdvertiseActivity, "rejected", Toast.LENGTH_SHORT).show()
                        endpointNames.remove(endpointId)
                    }
//                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> Toast.makeText(this@AdvertiseActivity, "rejected", Toast.LENGTH_SHORT).show()
//                    ConnectionsStatusCodes.STATUS_ERROR -> Toast.makeText(this@AdvertiseActivity, "error", Toast.LENGTH_SHORT).show()
//                    else -> Toast.makeText(this@AdvertiseActivity, "else", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDisconnected(endpointId: String) {
                //no data can be sent
            }

            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                endpointNames[endpointId] = connectionInfo.endpointName

                AlertDialog.Builder(this@AdvertiseActivity, R.style.AlertDialogCustom)
                    .setTitle("Accept connection to " + connectionInfo.endpointName)
                    .setMessage("Confirm the code matches on both deices: " + connectionInfo.authenticationToken)
                    .setPositiveButton("Accept") { _, _ ->
                        connectionsClient.acceptConnection(endpointId, payloadCallback)
                    }.setNegativeButton(android.R.string.cancel) {_, _ ->
                        connectionsClient.rejectConnection(endpointId)
                    }.setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }

        connectionsClient
            .startAdvertising("nickName2", "pl.antonic.partify", mConnectionLifecycleCallback, advertisingOptions)
            .addOnSuccessListener {  }.addOnFailureListener {  }
    }
}
