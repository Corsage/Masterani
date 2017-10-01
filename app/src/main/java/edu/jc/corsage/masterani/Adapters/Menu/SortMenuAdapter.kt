package edu.jc.corsage.masterani.Adapters.Menu

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 10/1/2017.
 */

class SortMenuAdapter(val mContext: Context,
                      val mListDataHeader: List<ExpandedMenuModel>,
                      val mListDataChild: HashMap<ExpandedMenuModel, List<String>>,
                      val expandList: ExpandableListView): BaseExpandableListAdapter() {

    val layoutInflater: LayoutInflater = this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getGroupCount(): Int {
        return this.mListDataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        var childCount = 0
        if (groupPosition != 2) {
            childCount = this.mListDataChild[this.mListDataHeader[groupPosition]]!!.size
        }
        return childCount
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.mListDataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.mListDataChild[this.mListDataHeader[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val headerTitle = getGroup(groupPosition) as ExpandedMenuModel

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_sort_headers, null)
        } else {
            view = convertView
        }

        val tvHeader: TextView = view.findViewById(R.id.submenu)
        tvHeader.setTypeface(null, Typeface.BOLD)
        tvHeader.text = headerTitle.getIconName()

        val ivIcon: AppCompatImageView = view.findViewById<ImageView>(R.id.iconimage) as AppCompatImageView
        ivIcon.setImageResource(headerTitle.getIconImg())

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_sort_submenu, null)
        } else {
            view = convertView
        }

        val tvChild: TextView = view.findViewById(R.id.submenu)
        tvChild.text = childText

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}