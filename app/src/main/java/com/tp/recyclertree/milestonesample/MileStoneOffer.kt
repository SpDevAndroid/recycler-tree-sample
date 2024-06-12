package com.tp.recyclertree.milestonesample

data class MileStoneOffer(val amt: Int, val unitSpace: Int = 1) {
    val label: String get() = "${amt}k"
}
