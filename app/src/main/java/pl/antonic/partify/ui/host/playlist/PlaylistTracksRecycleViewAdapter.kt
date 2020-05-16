package pl.antonic.partify.ui.host.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.antonic.partify.R
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track

class PlaylistTracksRecycleViewAdapter(var dataSource: ObjectList<Track>, var currentlyPlaying: Int = -1
) : RecyclerView.Adapter<PlaylistTracksRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(playlistTrackRowView: View) : RecyclerView.ViewHolder(playlistTrackRowView) {
        val trackPhoto = itemView.findViewById<ImageView>(R.id.playlistTrackPhoto)
        val songName = itemView.findViewById<TextView>(R.id.playlistSongName)
        val artistName = itemView.findViewById<TextView>(R.id.playlistArtistName)
        val playlistCardView = itemView.findViewById<CardView>(R.id.playlistCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val playlistTrackView = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_track_row, parent, false)
        return ViewHolder(playlistTrackView)
    }

    override fun getItemCount(): Int {
        return if (dataSource.items == null) 0 else dataSource.items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlistTrackSelector = holder.itemView.context as PlaylistTrackSelector
        dataSource.items!![position].apply {
            holder.playlistCardView.setOnClickListener {
                playlistTrackSelector.playSelectedTrack(position)
            }

            if (currentlyPlaying == position) {
                holder.playlistCardView.setCardBackgroundColor(ResourcesCompat.getColor(holder.itemView.context.resources, R.color.cardViewSecondaryColor, null))
            } else {
                holder.playlistCardView.setCardBackgroundColor(ResourcesCompat.getColor(holder.itemView.context.resources, R.color.cardViewColor, null))
            }

            Picasso.get().load(album!!.images!![0].url).into(holder.trackPhoto)
            holder.songName.text = name

            val all_artists = mutableListOf<String>()

            for (artist in artists!!) {
                all_artists.add(artist.name!!)
            }

            holder.artistName.text = all_artists.joinToString()
        }
    }
}