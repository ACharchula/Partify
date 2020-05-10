package pl.antonic.partify.ui.common.selection.genres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import pl.antonic.partify.R
import pl.antonic.partify.model.common.SeedType
import pl.antonic.partify.model.common.SelectableGenres
import pl.antonic.partify.ui.common.selection.SeedListModificator

class GenreRecycleViewAdapter(private val dataSource: SelectableGenres)
    : RecyclerView.Adapter<GenreRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(genreRow: View) : RecyclerView.ViewHolder(genreRow) {
        val genreName = itemView.findViewById<TextView>(R.id.genreName)
        val genreCardView = itemView.findViewById<CardView>(R.id.genreCardView)
        val genreCheckBox = itemView.findViewById<CheckBox>(R.id.genreCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val genreRowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_row, parent, false)
        return ViewHolder(genreRowView)
    }

    override fun getItemCount(): Int {
        return if (dataSource.genres == null) 0 else dataSource.genres!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModificator = holder.itemView.context as SeedListModificator
        dataSource.genres!![position].apply {

            holder.genreCheckBox.setOnClickListener {
                if (!holder.genreCheckBox.isChecked) {
                    listModificator.removeId(name, SeedType.GENRE)
                    selected = false
                } else {
                    if(!listModificator.addId(name, SeedType.GENRE)) {
                        holder.genreCheckBox.isChecked = false
                        selected = false
                    } else {
                        selected = true
                    }
                }
            }

            holder.genreCardView.setOnClickListener {
                if (holder.genreCheckBox.isChecked) {
                    listModificator.removeId(name, SeedType.GENRE)
                    holder.genreCheckBox.isChecked = false
                    selected = false
                } else {
                    if(listModificator.addId(name, SeedType.GENRE)) {
                        holder.genreCheckBox.isChecked = true
                        selected = true
                    }

                }
            }

            holder.genreCheckBox.isChecked = selected
            holder.genreName.text = name
        }
    }
}