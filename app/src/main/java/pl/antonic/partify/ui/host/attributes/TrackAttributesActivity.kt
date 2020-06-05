package pl.antonic.partify.ui.host.attributes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_track_attributes.*
import pl.antonic.partify.R
import pl.antonic.partify.model.common.Attributes
import pl.antonic.partify.model.common.Seeds
import pl.antonic.partify.model.spotify.Playlist
import pl.antonic.partify.ui.host.playlist.PlaylistActivity

class TrackAttributesActivity : AppCompatActivity() {

    private lateinit var finalSeeds : Seeds
    private lateinit var viewModel: TrackAttributesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_attributes)
        lockRotation()

        viewModel = ViewModelProvider(this).get(TrackAttributesViewModel::class.java)
        finalSeeds = intent.getSerializableExtra("final_seeds") as Seeds
        setExpandables()
        setSeekBars()
        setObservers()

        attributesNextButton.setOnClickListener {
            attributesNextButton.visibility = View.GONE
            loadingRecommendations.visibility = View.VISIBLE
            viewModel.getRecommendations(finalSeeds, getFinalAttributes())
        }

        viewModel.playlist.observe(this, Observer {
            attributesNextButton.visibility = View.VISIBLE
            loadingRecommendations.visibility = View.GONE
            if (it != null) {
                val intent = Intent(this, PlaylistActivity::class.java)
                intent.putExtra("playlist", viewModel.playlist.value as Playlist)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Couldn't find songs for this attributes - please change them.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFinalAttributes() : Attributes {
        val attributes = Attributes(null, null, null, null, null, null, null)
        if (limitExpand.rotation == -180F)
            attributes.limit = viewModel.attr.value?.limit
        if (acousticnessExpand.rotation == -180F)
            attributes.acousticness = viewModel.attr.value?.acousticness
        if (danceabilityExpand.rotation == -180F)
            attributes.danceability = viewModel.attr.value?.danceability
        if (energyExpand.rotation == -180F)
            attributes.energy = viewModel.attr.value?.energy
        if (instrumentalnessExpand.rotation == -180F)
            attributes.instrumentalness = viewModel.attr.value?.instrumentalness
        if (popularityExpand.rotation == -180F)
            attributes.popularity = viewModel.attr.value?.popularity
        if (valenceExpand.rotation == -180F)
            attributes.valence = viewModel.attr.value?.valence
        return attributes
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockRotation() {
        val currentOrientation = this.resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun setObservers() {
        viewModel.attr.observe(this, Observer {
            limitSeekBarState.text = it.limit
            acSeekBarState.text = it.acousticness
            dcSeekBarState.text = it.danceability
            enSeekBarState.text = it.energy
            inSeekBarState.text = it.instrumentalness
            popSeekBarState.text = it.popularity
            vSeekBarState.text = it.valence
        })
    }

    private fun limitExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (limitExpand.rotation == -180F) 0F else -180F
        limitExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            limitTarget.visibility = View.VISIBLE
        } else {
            limitTarget.visibility = View.GONE
        }
    }

    private fun setExpandables() {
        limitCV.setOnClickListener {
            limitExpandable()
        }

        limitExpand.setOnClickListener {
            limitExpandable()
        }

        acousticnessExpand.setOnClickListener {
            acExpandable()
        }

        acCV.setOnClickListener {
            acExpandable()
        }

        dcCV.setOnClickListener {
            dcExpandable()
        }

        danceabilityExpand.setOnClickListener {
            dcExpandable()
        }

        energyExpand.setOnClickListener {
            enExpandable()
        }

        enCV.setOnClickListener {
            enExpandable()
        }

        inCV.setOnClickListener {
            inExpandable()
        }

        instrumentalnessExpand.setOnClickListener {
            inExpandable()
        }

        poCV.setOnClickListener {
            poExpandable()
        }

        popularityExpand.setOnClickListener {
            poExpandable()
        }

        vCV.setOnClickListener {
            vExpandable()
        }
        valenceExpand.setOnClickListener {
            vExpandable()
        }
    }

    private fun setSeekBars() {
        limitSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeLimit(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        acSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeAcousticness(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        dcSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeDanceability(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        enSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeEnergy(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        inSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeInstrumentalness(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        popSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changePopularity(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        vSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.changeValence(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun acExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (acousticnessExpand.rotation == -180F) 0F else -180F
        acousticnessExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            acTarget.visibility = View.VISIBLE
        } else {
            acTarget.visibility = View.GONE
        }
    }

    private fun dcExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (danceabilityExpand.rotation == -180F) 0F else -180F
        danceabilityExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            dcTarget.visibility = View.VISIBLE
        } else {
            dcTarget.visibility = View.GONE
        }
    }

    private fun enExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (energyExpand.rotation == -180F) 0F else -180F
        energyExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            enTarget.visibility = View.VISIBLE
        } else {
            enTarget.visibility = View.GONE
        }
    }

    private fun inExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (instrumentalnessExpand.rotation == -180F) 0F else -180F
        instrumentalnessExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            inTarget.visibility = View.VISIBLE
        } else {
            inTarget.visibility = View.GONE
        }
    }

    private fun poExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (popularityExpand.rotation == -180F) 0F else -180F
        popularityExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            popTarget.visibility = View.VISIBLE
        } else {
            popTarget.visibility = View.GONE
        }
    }

    private fun vExpandable() {
        TransitionManager.beginDelayedTransition(attributesLinearLayout)

        val deg = if (valenceExpand.rotation == -180F) 0F else -180F
        valenceExpand.animate().rotation(deg).interpolator =
            AccelerateDecelerateInterpolator()

        if (deg == -180F) {
            vTarget.visibility = View.VISIBLE
        } else {
            vTarget.visibility = View.GONE
        }
    }
}
