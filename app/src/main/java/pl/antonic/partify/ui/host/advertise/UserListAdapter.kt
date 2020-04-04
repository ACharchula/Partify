package pl.antonic.partify.ui.host.advertise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import pl.antonic.partify.R
import pl.antonic.partify.model.common.UserSelections

class UserListAdapter(private val context: Context,
                      private val dataSource: List<UserSelections>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.user_row, parent, false)

        val userName = rowView.findViewById<TextView>(R.id.userName)
        val seedLoading = rowView.findViewById<ProgressBar>(R.id.seedLoading)
        val doneIcon = rowView.findViewById<ImageView>(R.id.doneIcon)

        val user = getItem(position) as UserSelections

        userName.text = user.userName

        if (user.seeds == null) {
            seedLoading.visibility = View.VISIBLE
            doneIcon.visibility = View.GONE
        } else {
            seedLoading.visibility = View.GONE
            doneIcon.visibility = View.VISIBLE
        }


        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}