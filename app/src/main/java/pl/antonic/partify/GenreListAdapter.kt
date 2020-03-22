package pl.antonic.partify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.antonic.partify.spotify.api.model.Artist
import pl.antonic.partify.spotify.api.model.Genres
import pl.antonic.partify.spotify.api.model.ObjectList

class GenreListAdapter(private val context: Context,
                       private val dataSource: Genres
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.genre_row, parent, false)

        val genreName = rowView.findViewById(R.id.genreName) as TextView
        val genre = getItem(position) as String
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