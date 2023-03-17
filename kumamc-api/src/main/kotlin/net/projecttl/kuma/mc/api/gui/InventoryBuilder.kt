package net.projecttl.kuma.mc.api.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.projecttl.kuma.mc.api.gui.util.Slot
import java.util.UUID

// Code for https://github.com/devproje/InventoryGUI.git
interface InventoryBuilder {
    val player: Player
    val slotType: InventoryType
    val title: Component
    val id: UUID
    val inventory: Inventory
    val slots: HashMap<Int, out Slot>

    fun build(): Inventory

    fun slot(slot: Int, item: ItemStack, handler: InventoryPreClickEvent.() -> Unit)

    fun slot(slot: Int, item: ItemStack)

    fun onClose(handler: InventoryCloseEvent.() -> Unit)

    fun close()

    fun destroy()
}