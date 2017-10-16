package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by j3chowdh on 9/10/2017.
 */

@Parcelize
data class Sort(val total: Int,
                val per_page: Int,
                val current_page: Int,
                val last_page: Int,
                val next_page_url: String?,
                val prev_page_url: String?,
                val from: Int,
                val to: Int,
                val data: List<SortAnime>) : Parcelable