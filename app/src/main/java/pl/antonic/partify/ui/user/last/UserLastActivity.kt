package pl.antonic.partify.ui.user.last

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_final.*
import pl.antonic.partify.R
import pl.antonic.partify.service.common.NearbyClientService
import pl.antonic.partify.ui.common.menu.MenuActivity

class UserLastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_final)
        NearbyClientService.get().stopAllEndpoints();
        goToMenuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onBackPressed() {}
}
