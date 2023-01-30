package net.projecttl.kuma.mc.api.listeners

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.item.ItemStack
import net.projecttl.kuma.mc.api.AreaUtils
import net.projecttl.kuma.mc.api.Elevator

class Elevators(private val evInfo: MutableList<Elevator>) {
    private fun action(player: Player, act: (Elevator) -> Unit): Boolean {
        evInfo.forEach { ev ->
            ev.apply {
                try {
                    val area = player.current()
                    AreaUtils(area, true).apply {
                        if (player.inArea()) {
                            act(ev)
                            return true
                        }
                    }
                } catch (ex: Exception) {
                    MinecraftServer.getExceptionManager().handleException(ex)
                }
            }
        }

        return false
    }

    fun run(node: EventNode<Event>) {
        fun Player.itemExist(): Boolean {
            if (!inventory.getItemStack(7).isAir && !inventory.getItemStack(8).isAir) {
                return true
            }

            return false
        }

        fun Player.removeItem() {
            if (!itemExist()) {
                return
            }

            inventory.setItemStack(7, ItemStack.AIR)
            inventory.setItemStack(8, ItemStack.AIR)
        }

        node.addListener(PlayerMoveEvent::class.java) { event ->
            val act = action(event.player) { ev ->
                ev.apply {
                    event.player.swapItem()
                }
            }

            if (!act) {
                event.player.removeItem()
            }
        }

        node.addListener(PlayerUseItemEvent::class.java) { event ->
            action(event.player) { ev ->
                ev.apply {
                    when (event.itemStack.displayName) {
                        upper.displayName -> {
                            event.player.next()
                        }

                        down.displayName -> {
                            event.player.prev()
                        }
                    }
                }
            }
        }

        node.addListener(InventoryPreClickEvent::class.java) { event ->
            action(event.player) {
                event.isCancelled = true
            }
        }

        node.addListener(ItemDropEvent::class.java) { event ->
            action(event.player) {
                event.isCancelled = true
            }
        }

        node.addListener(PlayerSwapItemEvent::class.java) { event ->
            action(event.player) {
                event.isCancelled = true
            }
        }
    }
}