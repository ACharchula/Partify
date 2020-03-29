package pl.antonic.partify.list.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import pl.antonic.partify.R
import pl.antonic.partify.SeedType
import pl.antonic.partify.activities.SeedListModificator
import pl.antonic.partify.spotify.api.model.Genres

class GenreListAdapter(private val context: Context,
                       private val dataSource: Genres) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.genre_row, parent, false)

        val genreName = rowView.findViewById(R.id.genreName) as TextView
        val genreCardView = rowView.findViewById<CardView>(R.id.genreCardView)
        val genreCheckBox = rowView.findViewById<CheckBox>(R.id.genreCheckBox)

        val genre = getItem(position) as String

        val listModificator = context as SeedListModificator

        genreCheckBox.setOnClickListener {
            if (!genreCheckBox.isChecked) {
                listModificator.removeId(genre, SeedType.GENRE)
            } else {
                if(!listModificator.addId(genre, SeedType.GENRE))
                    genreCheckBox.isChecked = false
            }
        }

        genreCardView.setOnClickListener {
            if (genreCheckBox.isChecked) {
                listModificator.removeId(genre, SeedType.GENRE)
                genreCheckBox.isChecked = false
            } else {
                if(listModificator.addId(genre, SeedType.GENRE))
                    genreCheckBox.isChecked = true

            }
        }

        genreCheckBox.isChecked = listModificator.containsId(genre, SeedType.GENRE)
        genreName.text = genre
        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource.genres!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (dataSource.genres == null) 0 else dataSource.genres!!.size
    }
}