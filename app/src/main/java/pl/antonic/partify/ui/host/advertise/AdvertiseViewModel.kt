package pl.antonic.partify.ui.host.advertise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.common.UserSelections

class AdvertiseViewModel : ViewModel() {

    //TODO is live data needed here?
    private val _allSeeds = MutableLiveData<MutableList<UserSelections>>()
        .apply { value = mutableListOf() }
    val allSeeds: LiveData<MutableList<UserSelections>>
        get() = _allSeeds

    var isAdvertising = false

    public fun addUserSelections(userSelections: UserSelections) {
        _allSeeds.value!!.add(userSelections)
    }

    public fun getCombinedSeeds() : Seeds {
        var seeds = Seeds()
        for (user in allSeeds.value!!) {
            if (user.seeds != null)
                seeds = seeds.combineSeeds(user.seeds!!)
        }
        return seeds
    }

    fun updateUserSeeds(userId: String, payload: Payload) {
        if (payload.asBytes() != null) {
            val seeds = Gson().fromJson(String(payload.asBytes()!!), Seeds::class.java)

            for (user in allSeeds.value!!) {
                if (user.userId == userId) {
                    user.seeds = seeds
                    break
                }
            } //TODO what if there is no such endpoint id?
        }
    }

    fun remove(userId: String) {
        allSeeds.value!!.removeIf { user -> user.userId == userId}
    }

    fun removeIfNotCompleted(userId: String) : Boolean {
        return allSeeds.value!!.removeIf { user -> user.userId == userId && user.seeds == null}
    }

}