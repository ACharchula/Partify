package pl.antonic.partify

import com.google.android.gms.nearby.connection.ConnectionsClient

class NearbyClient {
    companion object {
        private lateinit var connectionsClient: ConnectionsClient

        public fun set(connectionsClient: ConnectionsClient) {
            this.connectionsClient = connectionsClient
        }

        public fun get() : ConnectionsClient {
            return this.connectionsClient
        }
    }
}