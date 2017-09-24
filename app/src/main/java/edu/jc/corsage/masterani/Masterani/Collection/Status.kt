package edu.jc.corsage.masterani.Masterani.Collection

/**
 * Contains all possible status types for Masterani, and their associated values.
 */

data class Status(private val status: Int) {

    override fun toString(): String  = when (status) {
        0 ->  "Completed"
        1 -> "Ongoing"
        2 -> "Un-aired"
        else -> "Unknown"
    }
}