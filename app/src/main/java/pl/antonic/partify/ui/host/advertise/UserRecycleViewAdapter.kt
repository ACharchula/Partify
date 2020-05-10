package pl.antonic.partify.ui.host.advertise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.antonic.partify.R
import pl.antonic.partify.model.common.UserSelections

class UserRecycleViewAdapter(var dataSource: List<UserSelections>) : RecyclerView.Adapter<UserRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(userRowView: View) : RecyclerView.ViewHolder(userRowView) {
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val seedLoading = itemView.findViewById<ProgressBar>(R.id.seedLoading)
        val doneIcon = itemView.findViewById<ImageView>(R.id.doneIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val userRowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row, parent, false)
        return ViewHolder(userRowView)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataSource[position].apply {
            holder.userName.text = userName

            if (seeds == null) {
                holder.seedLoading.visibility = View.VISIBLE
                holder.doneIcon.visibility = View.GONE
            } else {
                holder.seedLoading.visibility = View.GONE
                holder.doneIcon.visibility = View.VISIBLE
            }
        }
    }
}