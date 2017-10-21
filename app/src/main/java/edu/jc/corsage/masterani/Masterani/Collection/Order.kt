package edu.jc.corsage.masterani.Masterani.Collection

/**
 * Contains all possible order types for Masterani, and their associated values.
 */

enum class Order(private val order: String) {
    SCORE_HIGH("score_desc"),
    SCORE_LOW("score"),
    TITLE_ASCEND("title"),
    TITLE_DESCEND("title_desc");

    companion object {
        fun from(findValue: Int): Order = Order.valueOf("x$findValue")
    }

    // Get enum string value.
    override fun toString(): String {
        return order
    }
}