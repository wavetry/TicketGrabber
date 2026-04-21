package com.ticket.grabber.ui.viewmodel

/**
 * 搜索参数
 */
data class SearchParams(
    val fromStation: String = "",
    val toStation: String = "",
    val departureDate: String = "",
    val trainType: String? = null
) {
    fun isValid(): Boolean {
        return fromStation.isNotEmpty() && toStation.isNotEmpty() && departureDate.isNotEmpty()
    }
}

/**
 * 筛选条件
 */
data class FilterCriteria(
    val minDepartureTime: String? = null,
    val maxDepartureTime: String? = null,
    val minDuration: Long? = null,
    val maxDuration: Long? = null,
    val hasBusinessSeat: Boolean = false,
    val hasFirstClass: Boolean = false,
    val hasSecondClass: Boolean = false,
    val hasSeat: Boolean = false
) {
    fun shouldShow(train: TrainWithAvailability): Boolean {
        if (hasBusinessSeat && train.businessSeat <= 0) return false
        if (hasFirstClass && train.firstClassSeat <= 0) return false
        if (hasSecondClass && train.secondClassSeat <= 0) return false
        if (hasSeat && train.hardSeat <= 0 && train.salary <= 0) return false
        return true
    }
}

/**
 * 列车排序选项
 */
enum class TrainSortOption {
    BY_TIME,        // 按时间
    BY_DURATION,    // 按时长
    BY_PRICE,       // 按价格
    BY_SEATS        // 按余票
}
