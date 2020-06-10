package pl.antonic.partify.ui.common.selection.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.ui.common.selection.SeedListModificator
import pl.antonic.partify.model.spotify.ObjectList
import pl.antonic.partify.model.spotify.Track

class TrackRecycleViewAdapter(var dataSource: ObjectList<Track>)
    : RecyclerView.Adapter<TrackRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(trackRow: View) : RecyclerView.ViewHolder(trackRow) {
        val trackPhoto = itemView.findViewById<ImageView>(R.id.trackPhoto)
        val songName = itemView.findViewById<TextView>(R.id.songName)
        val artistName = itemView.findViewById<TextView>(R.id.artistName)
        val trackCardView = itemView.findViewById<CardView>(R.id.trackCardView)
        val trackCheckBox = itemView.findViewById<CheckBox>(R.id.trackCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val trackRowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_row, parent, false)
        return ViewHolder(trackRowView)
    }

    override fun getItemCount(): Int {
        return if (dataSource.items == null) 0 else dataSource.items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModificator = holder.itemView.context as SeedListModificator
        dataSource.items!![position].apply {

            holder.trackCheckBox.setOnClickListener {
                if (!holder.trackCheckBox.isChecked) {
                    listModificator.removeId(id!!, SeedType.TRACK)
                    selected = false
                } else {
                    if(!listModificator.addId(id!!, SeedType.TRACK)) {
                        holder.trackCheckBox.isChecked = false
                        selected = false
                    } else {
                        selected = true
                    }
                }
            }

            holder.trackCardView.setOnClickListener {
                if (holder.trackCheckBox.isChecked) {
                    listModificator.removeId(id!!, SeedType.TRACK)
                    holder.trackCheckBox.isChecked = false
                    selected = false
                } else {
                    if(listModificator.addId(id!!, SeedType.TRACK)) {
                        holder.trackCheckBox.isChecked = true
                        selected = true
                    }
                }
            }

            holder.trackCheckBox.isChecked = selected
            Picasso.get().load(album!!.images!![0].url).into(holder.trackPhoto)
            holder.songName.text = name

            val allArtists = mutableListOf<String>()

            for (artist in artists!!) {
                allArtists.add(artist.name!!)
            }

            holder.artistName.text = allArtists.joinToString()
        }
    }
}