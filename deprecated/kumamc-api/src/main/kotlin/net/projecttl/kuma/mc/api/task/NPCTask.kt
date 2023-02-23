package net.projecttl.kuma.mc.api.task

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.*
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.entity.hologram.Hologram
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.projecttl.kuma.mc.instance
import java.util.*

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
    val name: List<Component>,
    val skin: UUID,
    val loc: Pos,
    val handler: PlayerEntityInteractEvent.() -> Unit
)

class NPCTask(private val npc: NPCData) {
    private fun registerHandler(node: EventNode<Event>) {
        node.addListener(PlayerEntityInteractEvent::class.java) { event ->
            if (event.target.uuid != npc.uuid) {
                return@addListener
            }

            npc.handler(event)
        }
    }

    private fun getNamePosition(playerPosition: Pos, multiplier: Int): Pos {
        return playerPosition.add(0.0, 1.8 * (multiplier + 1), 0.0)
    }

    fun run(node: EventNode<Event>) {
        val i = instance
        registerHandler(node)

        npc.name.reversed().forEachIndexed { index, name ->
            Hologram(i, getNamePosition(npc.loc, index), name)
        }

        val passenger = Entity(EntityType.ARMOR_STAND).apply {
            val meta = entityMeta as ArmorStandMeta
            meta.setNotifyAboutChanges(false)
            meta.isSmall = true
            meta.isInvisible = true
        }

        passenger.setInstance(i)
        passenger.isAutoViewable = true

        if (npc.type != EntityType.PLAYER) {
            Entity(npc.type).apply {
                uuid = npc.uuid
                setInstance(i, npc.loc)
            }
            
            return
        }

        FakePlayer.initPlayer(npc.uuid, "") {
            it.entityMeta.isCapeEnabled = true
            it.entityMeta.isHatEnabled = true
            it.entityMeta.isJacketEnabled = true
            it.entityMeta.isRightSleeveEnabled = true
            it.entityMeta.isLeftSleeveEnabled = true
            it.entityMeta.isRightLegEnabled = true
            it.entityMeta.isLeftLegEnabled = true

            it.gameMode = GameMode.CREATIVE
            it.skin = PlayerSkin.fromUuid(npc.skin.toString())

            it.addPassenger(passenger)
            it.teleport(npc.loc)
        }
    }
}
