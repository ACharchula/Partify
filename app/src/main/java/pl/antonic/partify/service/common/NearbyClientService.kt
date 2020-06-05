package pl.antonic.partify.service.common

import com.google.android.gms.nearby.connection.ConnectionsClient

class NearbyClientService {
    companion object {
        private var connectionsClient: ConnectionsClient? = null

        fun set(connectionsClient: ConnectionsClient) {
            Companion.connectionsClient = connectionsClient
        }

        fun get() : ConnectionsClient {
            return connectionsClient!!
        }
    }
}