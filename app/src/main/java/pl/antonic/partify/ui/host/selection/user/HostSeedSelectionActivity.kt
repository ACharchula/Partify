package pl.antonic.partify.ui.host.selection.user

import android.content.Intent
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.ui.host.selection.all.AllSeedSelectionActivity
import pl.antonic.partify.ui.user.selection.UserSeedSelectionActivity

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
