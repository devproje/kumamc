package net.projecttl.kuma.mc.api.listeners

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.item.ItemStack
import net.projecttl.kuma.mc.api.utils.AreaUtils
import net.projecttl.kuma.mc.api.utils.Elevator

class Elevators(private val evInfo: MutableList<Elevator>) {
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
            evInfo.forEach { ev ->
                ev.apply {
                    try {
                        val area = event.player.current()
                        AreaUtils(area, true).apply {
                            if (event.player.inArea()) {
                                event.player.swapItem()
                                return@addListener
                            }
                        }
                    } catch (ex: NullPointerException) {
                        MinecraftServer.getExceptionManager().handleException(ex)
                    } catch (ex: NoSuchElementException) {
                        MinecraftServer.getExceptionManager().handleException(ex)
                    }
                }
            }

            event.player.removeItem()
        }

        node.addListener(PlayerUseItemEvent::class.java) { event ->
            evInfo.forEach { ev ->
                ev.apply {
                    try {
                        when (event.itemStack.displayName) {
                            upper.displayName -> {
                                event.player.next()
                            }

                            down.displayName -> {
                                event.player.prev()
                            }
                        }
                    } catch (ex: NoSuchElementException) {
                        MinecraftServer.getExceptionManager().handleException(ex)
                    }
                }
            }
        }
    }
}