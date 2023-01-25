package net.projecttl.kuma.mc.api.utils

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player

data class Area(val pos1: Vec, val pos2: Vec)

class AreaUtils(private val area: Area, private val height: Boolean) {
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

    fun Player.inArea(): Boolean {
        if (check(position.x, area.pos1.x, area.pos2.x) &&
            check(position.z, area.pos1.z, area.pos2.z)) {
            if (height) {
                if (check(position.y, area.pos1.y, area.pos2.y)) {
                    return true
                }

                return false
            }

            return true
        }

        return false
    }

    fun Pos.inArea(): Boolean {
        if (check(this.x, area.pos1.x, area.pos2.x) &&
            check(this.z, area.pos1.z, area.pos2.z)) {
            if (height) {
                if (check(this.y, area.pos1.y, area.pos2.y)) {
                    return true
                }

                return false
            }

            return true
        }

        return false
    }
}