package ch.skyfy.ghuperms.utils

import ch.skyfy.ghuperms.data.Group

object TestUtils {

    fun getGroupByName(mutableSet: Set<Group>, groupName: String): Group? {
        return mutableSet.firstOrNull { it.name == groupName }
    }

}