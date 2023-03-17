package net.projecttl.kuma.mc.api

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.InventoryType
import net.projecttl.kuma.mc.api.gui.InventoryBuilder
import net.projecttl.kuma.mc.api.model.Spawn
import java.util.*

var spawn: Spawn? = null
val inventoryIds = hashMapOf<UUID, InventoryBuilder>()

fun Player.gui(title: Component, slotType: InventoryType = InventoryType.CHEST_3_ROW) {}