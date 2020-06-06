package pl.antonic.partify.service.common

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload

class NearbyClientService {
    companion object {
        private var connectionsClient: ConnectionsClient? = null
        private var endpointId: String? = null

        fun set(connectionsClient: ConnectionsClient) {
            if (Companion.connectionsClient != null) {
                stopDiscoveringAndEraseConnectionsClient()
            }
            Companion.connectionsClient = connectionsClient
        }

        fun get() : ConnectionsClient {
            return connectionsClient!!
        }

        fun stopDiscoveringAndEraseConnectionsClient() {
            if (endpointId != null)
                connectionsClient?.disconnectFromEndpoint(endpointId!!)
            connectionsClient?.stopDiscovery()
            connectionsClient = null
        }

        fun addEndpoint(endpointId: String) {
            connectionsClient!!.stopDiscovery()
            Companion.endpointId = endpointId
        }

        fun send(bytesPayload: Payload) {
            connectionsClient!!.sendPayload(endpointId!!, bytesPayload)
        }
    }
}