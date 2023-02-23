package net.projecttl.kuma.mc.api.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

open class Elevator(val floor: ArrayList<Pair<Vec, Vec>>) {
    val upper = ItemStack.builder(Material.EMERALD)
        .displayName(
            Component.text("윗층으로", NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false))
        .build()
    val down = ItemStack.builder(Material.DIAMOND)
        .displayName(
            Component.text("아랫층으로", NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false))
        .build()
    private val none = ItemStack.builder(Material.BARRIER)
        .displayName(
            Component.text("사용불가", NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false))
        .build()

    fun Player.current(): Pair<Vec, Vec> {
        return floor.single { area ->
            area.first.x <= this@current.position.x && area.second.x >= this@current.position.x &&
                    area.first.y <= this@current.position.y && area.second.y >= this@current.position.y &&
                    area.first.z <= this@current.position.z && area.second.z >= this@current.position.z
        }
    }

    private fun Player.first(): Boolean {
        val now = current()
        if (floor.first() == now) {
            return true
        }

        return false
    }

    private fun Player.last(): Boolean {
        val now = current()
        if (floor.last() == now) {
            return true
        }

        return false
    }

    fun Player.next() {
        val cur = current()
        try {
            teleport(Pos(position.x, floor[floor.indexOf(cur) + 1].first.y, position.z, position.yaw, position.pitch))
        } catch (ex: IndexOutOfBoundsException) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }
        swapItem()
    }

    fun Player.prev() {
        val cur = current()
        try {
            teleport(Pos(position.x, floor[floor.indexOf(cur) - 1].first.y, position.z, position.yaw, position.pitch))
        } catch (ex: IndexOutOfBoundsException) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }
        swapItem()
    }

    fun Player.swapItem() {
        inventory.setItemStack(7, upper)
        inventory.setItemStack(8, down)

        if (first()) {
            inventory.setItemStack(8, none)
        }

        if (last()) {
            inventory.setItemStack(7, none)
        }
    }
}