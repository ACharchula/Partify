package pl.antonic.partify.service.common

import com.google.android.gms.nearby.connection.ConnectionsClient

class NearbyClientService {
    companion object {
        private lateinit var connectionsClient: ConnectionsClient

        public fun set(connectionsClient: ConnectionsClient) {
            Companion.connectionsClient = connectionsClient
        }

        public fun get() : ConnectionsClient {
            return connectionsClient
        }
    }
}