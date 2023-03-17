package net.projecttl.kuma.mc.api.gui

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryItemChangeEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.projecttl.kuma.mc.api.gui.util.Slot
import net.projecttl.kuma.mc.api.inventoryIds
import java.util.*

class SimpleInventoryBuilder(
    override val player: Player,
    override val slotType: InventoryType,
    override val title: Component
): InventoryBuilder {
    override val slots = hashMapOf<Int, Slot>()
    val closeHandlers = arrayListOf<InventoryCloseEvent.() -> Unit>()

    override val id: UUID = UUID.randomUUID()
    override lateinit var inventory: Inventory
        private set

    init {
        inventoryIds[id] = this
    }

    override fun slot(slot: Int, item: ItemStack, handler: InventoryPreClickEvent.() -> Unit) {
        slots[slot] = Slot(item, handler)
    }

    override fun onClose(handler: InventoryCloseEvent.() -> Unit) {
        closeHandlers.add(handler)
    }

    override fun slot(slot: Int, item: ItemStack) {
        slot(slot, item) {}
    }

    override fun close() {
        if(this::inventory.isInitialized)
            player.closeInventory()
    }

    override fun build() : Inventory {
        inventory = Inventory(slotType, title)
        for (slot in slots.entries) {
            inventory.setItemStack(slot.key, slot.value.stack)
        }

        player.openInventory(inventory)
        return inventory
    }

    private fun run(node: EventNode<Event>) {
        node.addListener(InventoryPreClickEvent::class.java) { event ->
            if(event.inventory?.title == this.title) {
                if (inventoryIds.contains(id) && event.cursorItem != null && event.player == player) {
                    if (event.inventory == inventory) {
                        for (slot in slots.entries) {
                            if (slot.key == event.slot){
                                event.isCancelled = true
                                slot.value.click(event)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun destroy() {
        if (player.openInventory?.title == inventory.title) {
            player.closeInventory()
        }
    }
}