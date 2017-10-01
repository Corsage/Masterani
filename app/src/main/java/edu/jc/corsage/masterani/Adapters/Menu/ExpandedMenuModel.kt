package edu.jc.corsage.masterani.Adapters.Menu

/**
 * Base class for custom expandable menu items.
 */

class ExpandedMenuModel {
    private var iconName: String = ""
    private var iconImg: Int = -1

    fun getIconName() : String {
        return this.iconName
    }

    fun setIconName(iconName: String) {
        this.iconName = iconName
    }

    fun getIconImg(): Int {
        return iconImg
    }

    fun setIconImg(iconImg: Int) {
        this.iconImg = iconImg
    }

}