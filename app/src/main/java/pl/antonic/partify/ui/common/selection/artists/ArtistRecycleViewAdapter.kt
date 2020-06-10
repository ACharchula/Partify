package pl.antonic.partify.ui.common.selection.artists

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
import pl.antonic.partify.model.spotify.Artist
import pl.antonic.partify.model.spotify.ObjectList

class ArtistRecycleViewAdapter(var dataSource: ObjectList<Artist>)
    : RecyclerView.Adapter<ArtistRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(artistRow: View) : RecyclerView.ViewHolder(artistRow) {
        val artistPhoto = itemView.findViewById(R.id.artistPhoto) as ImageView
        val artistName = itemView.findViewById(R.id.artistName) as TextView
        val artistCardView = itemView.findViewById<CardView>(R.id.artistCardView)
        val artistCheckBox = itemView.findViewById<CheckBox>(R.id.artistCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val artistRowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_row, parent, false)
        return ViewHolder(artistRowView)
    }

    override fun getItemCount(): Int {
        return if (dataSource.items == null) 0 else dataSource.items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModificator = holder.itemView.context as SeedListModificator
        dataSource.items!![position].apply {

            holder.artistCheckBox.setOnClickListener {
                if (!holder.artistCheckBox.isChecked) {
                    listModificator.removeId(id!!, SeedType.ARTIST)
                    selected = false
                } else {
                    if (!listModificator.addId(id!!, SeedType.ARTIST)) {
                        holder.artistCheckBox.isChecked = false
                        selected = false
                    } else {
                        selected = true
                    }
                }
            }

            holder.artistCardView.setOnClickListener {
                if (holder.artistCheckBox.isChecked) {
                    listModificator.removeId(id!!, SeedType.ARTIST)
                    holder.artistCheckBox.isChecked = false
                    selected = false
                } else {
                    if (listModificator.addId(id!!, SeedType.ARTIST)) {
                        holder.artistCheckBox.isChecked = true
                        selected = true
                    }
                }
            }

            holder.artistCheckBox.isChecked = selected
            Picasso.get().load(images!![0].url).into(holder.artistPhoto)
            holder.artistName.text = name
        }
    }
}