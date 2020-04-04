package pl.antonic.partify.ui.common.selection.artists

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
import pl.antonic.partify.model.spotify.Artist
import pl.antonic.partify.model.spotify.ObjectList

class ArtistListAdapter(private val context: Context,
                        private val dataSource: ObjectList<Artist>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.artist_row, parent, false)

        val artistPhoto = rowView.findViewById(R.id.artistPhoto) as ImageView
        val artistName = rowView.findViewById(R.id.artistName) as TextView
        val artistCardView = rowView.findViewById<CardView>(R.id.artistCardView)
        val artistCheckBox = rowView.findViewById<CheckBox>(R.id.artistCheckBox)

        val artist = getItem(position) as Artist

        val listModificator = context as SeedListModificator

        artistCheckBox.setOnClickListener {
            if (!artistCheckBox.isChecked) {
                listModificator.removeId(artist.id!!, SeedType.ARTIST)
            } else {
                if (!listModificator.addId(artist.id!!, SeedType.ARTIST))
                    artistCheckBox.isChecked = false
            }
        }

        artistCardView.setOnClickListener {
            if (artistCheckBox.isChecked) {
                listModificator.removeId(artist.id!!, SeedType.ARTIST)
                artistCheckBox.isChecked = false
            } else {
                if (listModificator.addId(artist.id!!, SeedType.ARTIST))
                    artistCheckBox.isChecked = true
            }
        }

        artistCheckBox.isChecked = listModificator.containsId(artist.id!!, SeedType.ARTIST)
        Picasso.get().load(artist.images!![0].url).into(artistPhoto)
        artistName.text = artist.name

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