package net.projecttl.kuma.mc.tasks

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
import net.projecttl.kuma.mc.listeners.instance
import net.projecttl.kuma.mc.api.utils.moveServer
import java.util.*

data class ServerNPC(
    val uniqueId: UUID = UUID.randomUUID(),
    val type: EntityType,
    val name: Component,
    val skin: String,
    val server: String,
    val loc: Pos,
)

class NPCTask(private val entityList: List<ServerNPC>) {
    init {
        if (entityList.isNotEmpty()) {
            entityList.forEach { npc ->
                if (npc.type != EntityType.PLAYER) {
                    val entity = Entity(npc.type).apply {
                        customName = npc.name
                        isCustomNameVisible = true
                    }

                    entity.setInstance(instance, npc.loc)
                    return@forEach
                }

                FakePlayer.initPlayer(npc.uniqueId, PlainTextComponentSerializer.plainText().serialize(npc.name)) {
                    it.entityMeta.isCapeEnabled = true
                    it.entityMeta.isHatEnabled = true
                    it.entityMeta.isJacketEnabled = true
                    it.entityMeta.isRightSleeveEnabled = true
                    it.entityMeta.isLeftSleeveEnabled = true
                    it.entityMeta.isRightLegEnabled = true
                    it.entityMeta.isLeftLegEnabled = true

                    it.customName = npc.name
                    it.isCustomNameVisible = true

                    it.skin = PlayerSkin.fromUuid(npc.skin)
                    it.teleport(npc.loc)
                }
            }
        }
    }

    fun run(node: EventNode<Event>) {
        if (entityList.isEmpty()) {
            return
        }

        node.addListener(PlayerEntityInteractEvent::class.java) { event ->
            entityList.forEach { npc ->
                if (npc.server == "") {
                    return@addListener
                }

                if (event.target.customName == npc.name) {
                    event.player.sendMessage("${npc.server}로 이동합니다.")
                    event.player.moveServer(npc.server)
                }
            }
        }
    }
}