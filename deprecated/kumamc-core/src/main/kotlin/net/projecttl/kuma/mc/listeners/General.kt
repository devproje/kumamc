package net.projecttl.kuma.mc.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerCommandEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.projecttl.kuma.mc.Core
import net.projecttl.kuma.mc.instance
import net.projecttl.kuma.mc.logger
import net.projecttl.kuma.mc.owner
import net.projecttl.kuma.mc.api.util.perm.isPermed

object Listener {
    fun run(node: EventNode<Event>, core: Core) {
        val dim = DimensionType.builder(NamespaceID.from("fullbright"))
            .ambientLight(10000F)
            .height(416)
            .logicalHeight(200)
            .minY(-64)
            .build()

        MinecraftServer.getDimensionTypeManager().addDimension(dim)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer(dim)
        instance.chunkLoader = AnvilLoader(core.props().getProperty("level-name"))

        node.addListener(PlayerCommandEvent::class.java) { event ->
            val name = PlainTextComponentSerializer.plainText().serialize(event.player.name)
            logger.info("$name using command: /${event.command}")
        }

        node.addListener(PlayerSpawnEvent::class.java) { event ->
            try {
                if (event.player.uuid != owner) {
                    return@addListener
                }
            } catch (ex: UninitializedPropertyAccessException) {
                logger.warn(Component.text("owner uuid is null"))
            }

            event.player.isPermed = true
        }
    }
}