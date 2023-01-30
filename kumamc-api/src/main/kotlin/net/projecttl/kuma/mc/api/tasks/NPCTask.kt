package net.projecttl.kuma.mc.api.tasks

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.projecttl.kuma.mc.api.instance
import java.util.UUID

/**
 * @param uuid      the uuid is NPC random id
 * @param type      the type is NPC entity type
 * @param name      the name is NPC displayed name
 * @param skin      the skin is NPC displayed skin uuid
 * @param loc       the loc is npc spawn location
 * @param handler   the handler is what to do when a NPC is clicked
 */
data class NPCData(
    val uuid: UUID = UUID.randomUUID(),
    val type: EntityType = EntityType.PLAYER,
    val name: Component,
    val skin: UUID,
    val loc: Pos,
    val handler: (PlayerEntityInteractEvent) -> Unit
)

@OptIn(DelicateCoroutinesApi::class)
class NPCTask(private val npc: NPCData) {
    private fun handler(node: EventNode<Event>) {
        node.addListener(PlayerEntityInteractEvent::class.java) {
            if (it.entity.name != npc.name) {
                return@addListener
            }

            npc.handler
        }
    }

    fun run(node: EventNode<Event>) {
        if (npc.type != EntityType.PLAYER) {
            val entity = Entity(npc.type).apply {
                customName = npc.name
                isCustomNameVisible = true
            }

            entity.setInstance(instance, npc.loc)
            return
        }

        FakePlayer.initPlayer(npc.uuid, PlainTextComponentSerializer.plainText().serialize(npc.name)) {
            it.entityMeta.isCapeEnabled = true
            it.entityMeta.isHatEnabled = true
            it.entityMeta.isJacketEnabled = true
            it.entityMeta.isRightSleeveEnabled = true
            it.entityMeta.isLeftSleeveEnabled = true
            it.entityMeta.isRightLegEnabled = true
            it.entityMeta.isLeftLegEnabled = true

            it.customName = npc.name
            it.isCustomNameVisible = true

            it.skin = PlayerSkin.fromUuid(npc.skin.toString())
            it.teleport(npc.loc)
        }

        GlobalScope.launch {
            handler(node)
        }
    }
}