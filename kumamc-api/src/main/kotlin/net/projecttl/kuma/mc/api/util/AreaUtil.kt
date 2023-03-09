package net.projecttl.kuma.mc.api.util

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

open class AreaUtil(private val area: Pair<Vec, Vec>, private val height: Boolean = false) {
    private fun swapNumber(pos1: Double, pos2: Double): Pair<Double, Double> {
        if (pos1 < pos2) {
            return Pair(pos1, pos2)
        }

        return Pair(pos2, pos1)
    }

    private fun check(target: Double, pos1: Double, pos2: Double): Boolean {
        val swap = swapNumber(pos1, pos2)
        if (target in swap.first..swap.second) {
            return true
        }

        return false
    }

    fun Pos.inArea(): Boolean {
        if (height && !check(y, area.first.y, area.second.y)) {
            return false
        }

        if (check(x, area.first.x, area.second.x) && check(z, area.first.z, area.second.z)) {
            return true
        }

        return false
    }
}