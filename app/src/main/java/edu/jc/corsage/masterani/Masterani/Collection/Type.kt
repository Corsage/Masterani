package edu.jc.corsage.masterani.Masterani.Collection

/**
 * Contains all possible anime types for Masterani, and their associated values.
 */

data class Type(val type: Int) {

    override fun toString(): String  = when (type) {
        0 ->  "TV Series"
        1 -> "OVA"
        2 -> "Movie"
        3 -> "Special"
        4 -> "ONA"
        else -> "Unknown"
    }
}