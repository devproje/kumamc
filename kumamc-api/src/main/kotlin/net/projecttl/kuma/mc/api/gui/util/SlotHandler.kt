package net.projecttl.kuma.mc.api.gui.util

import net.minestom.server.event.inventory.InventoryPreClickEvent

// Code for https://github.com/devproje/InventoryGUI.git
class SlotHandler {
    val click = arrayListOf<(InventoryPreClickEvent) -> Unit>()

    fun onClick(action: (InventoryPreClickEvent) -> Unit) {
        click.add(action)
    }
}