package pl.antonic.partify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.antonic.partify.spotify.api.model.ObjectList
import pl.antonic.partify.spotify.api.model.Track

class TrackListAdapter(private val context: Context,
                        private val dataSource: ObjectList<Track>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.track_row, parent, false)

        val trackPhoto = rowView.findViewById(R.id.trackPhoto) as ImageView
        val songName = rowView.findViewById(R.id.songName) as TextView
        val artistName = rowView.findViewById(R.id.artistName) as TextView

        val track = getItem(position) as Track

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