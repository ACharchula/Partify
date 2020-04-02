package pl.antonic.partify.activities.host

import android.content.Intent
import pl.antonic.partify.Seeds
import pl.antonic.partify.activities.user.UserSeedSelectionActivity

class HostSeedSelectionActivity : UserSeedSelectionActivity() {

    private lateinit var allSeeds : Seeds

    override fun getExtraData() {
        allSeeds = intent.getSerializableExtra("all_seeds") as Seeds
    }

    override fun nextButtonOnClick() {
        val intent = Intent(this, AllSeedSelectionActivity::class.java)
        intent.putExtra("all_seeds", allSeeds.combineSeeds(getSeeds()))
        startActivity(intent)
    }
}
