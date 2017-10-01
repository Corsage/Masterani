package edu.jc.corsage.masterani.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 10/1/2017.
 */

class MALFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_mal, container, false)

        return view
    }
}
