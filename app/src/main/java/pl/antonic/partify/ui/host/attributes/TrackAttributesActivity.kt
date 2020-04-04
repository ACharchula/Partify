package pl.antonic.partify.ui.host.attributes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_track_attributes.*
import pl.antonic.partify.R

class TrackAttributesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_attributes)

        acousticnessExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (acousticnessExpand.rotation == -180F) 0F else -180F
            acousticnessExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                acMin.visibility = View.VISIBLE
                acTarget.visibility = View.VISIBLE
                acMax.visibility = View.VISIBLE
            } else {
                acMin.visibility = View.GONE
                acTarget.visibility = View.GONE
                acMax.visibility = View.GONE
            }
        }

        danceabilityExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (danceabilityExpand.rotation == -180F) 0F else -180F
            danceabilityExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                dcMin.visibility = View.VISIBLE
                dcTarget.visibility = View.VISIBLE
                dcMax.visibility = View.VISIBLE
            } else {
                dcMin.visibility = View.GONE
                dcTarget.visibility = View.GONE
                dcMax.visibility = View.GONE
            }
        }

        durationExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (durationExpand.rotation == -180F) 0F else -180F
            durationExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                drMin.visibility = View.VISIBLE
                drTarget.visibility = View.VISIBLE
                drMax.visibility = View.VISIBLE
            } else {
                drMin.visibility = View.GONE
                drTarget.visibility = View.GONE
                drMax.visibility = View.GONE
            }
        }

        energyExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (energyExpand.rotation == -180F) 0F else -180F
            energyExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                enMin.visibility = View.VISIBLE
                enTarget.visibility = View.VISIBLE
                enMax.visibility = View.VISIBLE
            } else {
                enMin.visibility = View.GONE
                enTarget.visibility = View.GONE
                enMax.visibility = View.GONE
            }
        }

        instrumentalnessExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (instrumentalnessExpand.rotation == -180F) 0F else -180F
            instrumentalnessExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                insMin.visibility = View.VISIBLE
                insTarget.visibility = View.VISIBLE
                insMax.visibility = View.VISIBLE
            } else {
                insMin.visibility = View.GONE
                insTarget.visibility = View.GONE
                insMax.visibility = View.GONE
            }
        }

        keyExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (keyExpand.rotation == -180F) 0F else -180F
            keyExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                keyMin.visibility = View.VISIBLE
                keyTarget.visibility = View.VISIBLE
                keyMax.visibility = View.VISIBLE
            } else {
                keyMin.visibility = View.GONE
                keyTarget.visibility = View.GONE
                keyMax.visibility = View.GONE
            }
        }

        livenessExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (livenessExpand.rotation == -180F) 0F else -180F
            livenessExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                lvMin.visibility = View.VISIBLE
                lvTarget.visibility = View.VISIBLE
                lvMax.visibility = View.VISIBLE
            } else {
                lvMin.visibility = View.GONE
                lvTarget.visibility = View.GONE
                lvMax.visibility = View.GONE
            }
        }

        loudnessExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (loudnessExpand.rotation == -180F) 0F else -180F
            loudnessExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                ldMin.visibility = View.VISIBLE
                ldTarget.visibility = View.VISIBLE
                ldMax.visibility = View.VISIBLE
            } else {
                ldMin.visibility = View.GONE
                ldTarget.visibility = View.GONE
                ldMax.visibility = View.GONE
            }
        }

        modeExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (modeExpand.rotation == -180F) 0F else -180F
            modeExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                modeMin.visibility = View.VISIBLE
                modeTarget.visibility = View.VISIBLE
                modeMax.visibility = View.VISIBLE
            } else {
                modeMin.visibility = View.GONE
                modeTarget.visibility = View.GONE
                modeMax.visibility = View.GONE
            }
        }

        popularityExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (popularityExpand.rotation == -180F) 0F else -180F
            popularityExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                popMin.visibility = View.VISIBLE
                popTarget.visibility = View.VISIBLE
                popMax.visibility = View.VISIBLE
            } else {
                popMin.visibility = View.GONE
                popTarget.visibility = View.GONE
                popMax.visibility = View.GONE
            }
        }

        speechinessExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (speechinessExpand.rotation == -180F) 0F else -180F
            speechinessExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                spMin.visibility = View.VISIBLE
                spTarget.visibility = View.VISIBLE
                spMax.visibility = View.VISIBLE
            } else {
                spMin.visibility = View.GONE
                spTarget.visibility = View.GONE
                spMax.visibility = View.GONE
            }
        }

        tempoExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (tempoExpand.rotation == -180F) 0F else -180F
            tempoExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                tmMin.visibility = View.VISIBLE
                tmTarget.visibility = View.VISIBLE
                tmMax.visibility = View.VISIBLE
            } else {
                tmMin.visibility = View.GONE
                tmTarget.visibility = View.GONE
                tmMax.visibility = View.GONE
            }
        }

        timeSignatureExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (timeSignatureExpand.rotation == -180F) 0F else -180F
            timeSignatureExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                tsMin.visibility = View.VISIBLE
                tsTarget.visibility = View.VISIBLE
                tsMax.visibility = View.VISIBLE
            } else {
                tsMin.visibility = View.GONE
                tsTarget.visibility = View.GONE
                tsMax.visibility = View.GONE
            }
        }

        valenceExpand.setOnClickListener {

            TransitionManager.beginDelayedTransition(attributesLinearLayout)

            val deg = if (valenceExpand.rotation == -180F) 0F else -180F
            valenceExpand.animate().rotation(deg).interpolator =
                AccelerateDecelerateInterpolator()

            if (deg == -180F) {
                vlMin.visibility = View.VISIBLE
                vlTarget.visibility = View.VISIBLE
                vlMax.visibility = View.VISIBLE
            } else {
                vlMin.visibility = View.GONE
                vlTarget.visibility = View.GONE
                vlMax.visibility = View.GONE
            }
        }
    }
}
