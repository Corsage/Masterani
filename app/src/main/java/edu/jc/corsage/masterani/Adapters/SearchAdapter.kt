package edu.jc.corsage.masterani.Adapters

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.R
import kotlinx.android.synthetic.main.item_search.view.*
import org.w3c.dom.Text

/**
 * Created by j3chowdh on 9/17/2017.
 */

class SearchAdapter(val context: Context, val listener: View.OnClickListener,  c: MatrixCursor?) : CursorAdapter(context, c, false) {
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view: View

        if (convertView == null) {
            view = this.inflater.inflate(R.layout.item_search, viewGroup, false)
        } else {
            view = convertView
        }
        this.mCursor.moveToPosition(position)
        val svh = SearchViewHolder(view)

        svh.title.text = this.mCursor.getString(this.mCursor.getColumnIndex("title"))
        svh.info.text = this.mCursor.getString(this.mCursor.getColumnIndex("info"))

        view.tag = this.mCursor.getInt(this.mCursor.getColumnIndex(BaseColumns._ID))
        return view
    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        // idk
    }

    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View? {
        // hmm...
        return null
    }

    private inner class SearchViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.searchTitle)
        val info: TextView = view.findViewById(R.id.searchInfo)

        init {
            view.setOnClickListener(listener)
        }
    }

}