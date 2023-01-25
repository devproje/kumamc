package net.projecttl.kuma.mc.api.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player

class VehicleData(
    val id: Int,
    val name: String,
    val upper: Area?,
    val lower: Area?,
    val upperPos: Pos,
    val lowerPos: Pos) {
    fun Player.next(traffic: List<VehicleData>, obj: String) {
        val next = traffic[id + 1]
        teleport(next.lowerPos)
        sendMessage(
            Component.text(next.name, NamedTextColor.YELLOW)
            .append(Component.text("${obj}에 도착 하였습니다.")))
    }

    fun Player.prev(traffic: List<VehicleData>, obj: String) {
        val prev = traffic[id - 1]
        teleport(prev.upperPos)
        sendMessage(Component.text(prev.name, NamedTextColor.YELLOW)
            .append(Component.text("${obj}에 도착 하였습니다.")))
    }
}