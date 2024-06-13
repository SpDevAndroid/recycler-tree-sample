package com.tp.recyclertree.milestonesample

data class MileStoneOffer(val amt: Int, val unitSpace: Int = 1, var viewType : ItemType = ItemType.OFFER) {
    val label: String get() = "${amt}k"
    enum class ItemType(val viewId : Int) {
        OFFER(1),
        EMPTY_SPACE(2)
    }
}
