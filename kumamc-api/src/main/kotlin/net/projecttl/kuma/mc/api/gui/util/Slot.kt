package net.projecttl.kuma.mc.api.gui.util

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack

// Code for https://github.com/devproje/InventoryGUI.git
open class Slot(val stack: ItemStack, val click: InventoryPreClickEvent.() -> Unit)