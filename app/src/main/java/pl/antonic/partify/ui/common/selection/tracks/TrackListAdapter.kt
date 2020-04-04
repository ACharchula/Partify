package pl.antonic.partify.ui.common.selection.tracks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.ui.common.selection.SeedListModificator
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track

class TrackListAdapter(private val context: Context,
                        private val dataSource: ObjectList<Track>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.track_row, parent, false)

        val trackPhoto = rowView.findViewById(R.id.trackPhoto) as ImageView
        val songName = rowView.findViewById(R.id.songName) as TextView
        val artistName = rowView.findViewById(R.id.artistName) as TextView
        val trackCardView = rowView.findViewById<CardView>(R.id.trackCardView)
        val trackCheckBox = rowView.findViewById<CheckBox>(R.id.trackCheckBox)

        val track = getItem(position) as Track

        val listModificator = context as SeedListModificator

        trackCheckBox.setOnClickListener {
            if (!trackCheckBox.isChecked) {
                listModificator.removeId(track.id!!, SeedType.TRACK)
            } else {
                if(!listModificator.addId(track.id!!, SeedType.TRACK))
                    trackCheckBox.isChecked = false
            }
        }

        trackCardView.setOnClickListener {
            if (trackCheckBox.isChecked) {
                listModificator.removeId(track.id!!, SeedType.TRACK)
                trackCheckBox.isChecked = false
            } else {
                if(listModificator.addId(track.id!!, SeedType.TRACK))
                    trackCheckBox.isChecked = true
            }
        }

        trackCheckBox.isChecked = listModificator.containsId(track.id!!, SeedType.TRACK)
        Picasso.get().load(track.album!!.images!![0].url).into(trackPhoto)
        songName.text = track.name

        val artists = mutableListOf<String>()

        for (artist in track.artists!!) {
            artists.add(artist.name!!)
        }

        artistName.text = artists.joinToString()

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource.items!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (dataSource.items == null) 0 else dataSource.items!!.size
    }

}