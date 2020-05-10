package pl.antonic.partify.ui.host.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso
import pl.antonic.partify.R
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track

class PlaylistTracksListAdapter(private val context: Context,
                                private val dataSource: ObjectList<Track>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var currentlyPlayingTrack = -1 //-1 stands for NONE, the rest is an index in dataSource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.playlist_track_row, parent, false)

        val trackPhoto = rowView.findViewById(R.id.playlistTrackPhoto) as ImageView
        val songName = rowView.findViewById(R.id.playlistSongName) as TextView
        val artistName = rowView.findViewById(R.id.playlistArtistName) as TextView
        val playlistCardView = rowView.findViewById<CardView>(R.id.playlistCardView)

        val track = getItem(position) as Track

        playlistCardView.setOnClickListener {
            currentlyPlayingTrack = position
        }

        Picasso.get().load(track.album!!.images!![0].url).into(trackPhoto)
        songName.text = track.name

        val artists = mutableListOf<String>()

        for (artist in track.artists!!) {
            artists.add(artist.name!!)
        }

        artistName.text = artists.joinToString()

        if (currentlyPlayingTrack == position) {
            playlistCardView.setCardBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.cardViewSecondaryColor, null))
        }

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

    public fun nextTrack() {
        currentlyPlayingTrack++
        notifyDataSetChanged()
    }

    public fun previousTrack() {
        currentlyPlayingTrack--
        notifyDataSetChanged()
    }

    public fun playAtIndex(i: Int) {
        currentlyPlayingTrack = 0
        notifyDataSetChanged()
    }
}