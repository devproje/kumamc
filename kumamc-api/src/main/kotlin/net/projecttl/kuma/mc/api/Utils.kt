package net.projecttl.kuma.mc.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.item.firework.FireworkEffect
import net.projecttl.kuma.mc.utils.AreaUtils
import net.projecttl.kuma.mc.utils.Elevator
import net.projecttl.kuma.mc.utils.VehicleData
import net.projecttl.kuma.mc.utils.isFly
import net.projecttl.kuma.mc.utils.moveServer
import net.projecttl.kuma.mc.utils.perm.isPermed
import net.projecttl.kuma.mc.utils.showFireworkWithDuration

fun String.toMini(): Component = MiniMessage.miniMessage().deserialize(this)

fun Player.isFly() {
    isFly()
}

fun Player.isPermed(): Boolean {
    return isPermed
}

fun Player.moveServer(server: String) {
    moveServer(server)
}

fun Collection<Player>.showFireworkWithDuration(
    instance: Instance,
    position: Pos,
    effects: MutableList<FireworkEffect>
) {
    showFireworkWithDuration(instance, position, effects)
}

class AreaUtils(area: Pair<Vec, Vec>, height: Boolean) : AreaUtils(area, height)

class Elevator(floor: ArrayList<Pair<Vec, Vec>>) : Elevator(floor)

class VehicleData(
    id: Int,
    name: String,
    upper: Pair<Vec, Vec>?,
    lower: Pair<Vec, Vec>?,
    upperPos: Pos,
    lowerPos: Pos
) : VehicleData(id, name, upper, lower, upperPos, lowerPos)