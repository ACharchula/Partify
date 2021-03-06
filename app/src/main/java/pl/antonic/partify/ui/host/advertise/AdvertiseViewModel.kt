package pl.antonic.partify.ui.host.advertise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.common.UserSelections

class AdvertiseViewModel : ViewModel() {

    private val _allSeeds = MutableLiveData<MutableList<UserSelections>>()
        .apply { value = mutableListOf() }
    val allSeeds: LiveData<MutableList<UserSelections>>
        get() = _allSeeds

    var isAdvertising = false

    fun addUserSelections(userSelections: UserSelections) {
        _allSeeds.value!!.add(userSelections)
        _allSeeds.value = _allSeeds.value
    }

    fun getCombinedSeeds() : Seeds {
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
                    _allSeeds.value = _allSeeds.value
                    break
                }
            }
        }
    }

    fun remove(userId: String) {
        allSeeds.value!!.removeIf { user -> user.userId == userId}
        _allSeeds.value = _allSeeds.value
    }

    fun removeIfNotCompleted(userId: String) : Boolean {
        return allSeeds.value!!.removeIf { user -> user.userId == userId && user.seeds == null}
    }

}