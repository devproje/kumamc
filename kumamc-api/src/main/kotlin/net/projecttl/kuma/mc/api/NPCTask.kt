package net.projecttl.kuma.mc.api

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerEntityInteractEvent
import java.util.UUID

data class ServerNPC(
    val uniqueId: UUID = UUID.randomUUID(),
    val type: EntityType,
    val name: Component,
    val skin: String,
    val server: String,
    val loc: Pos,
    val handler: (PlayerEntityInteractEvent) -> Unit
)

class NPCTask(val npc: ServerNPC, val node: EventNode<Event>) {
    fun run() {
        if (npc.type != EntityType.PLAYER) {
            val entity = Entity(npc.type).also {
                val meta = it.entityMeta
                meta.customName = npc.name
                meta
            }
        }

        node.addListener(PlayerEntityInteractEvent::class.java, npc.handler)
    }
}