package net.projecttl.kuma.mc.listener

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerCommandEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.projecttl.kuma.mc.api.spawn
import net.projecttl.kuma.mc.api.util.AreaUtil
import net.projecttl.kuma.mc.instance
import net.projecttl.kuma.mc.logger
import net.projecttl.kuma.mc.prop
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.min

object General {
    fun run(node: EventNode<Event>) {
        val dim = DimensionType.builder(NamespaceID.from("fullbright"))
            .ambientLight(10000F)
            .height(416)
            .logicalHeight(200)
            .minY(-64)
            .build()
        MinecraftServer.getDimensionTypeManager().addDimension(dim)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer(dim)
        if (Files.exists(Path.of("./${prop.world}"))) {
            instance.chunkLoader = AnvilLoader(prop.world)
        } else {
            instance.setGenerator {
                val start = it.absoluteStart()
                val size = it.size()
                for (x in 0 until size.blockX()) {
                    for (z in 0 until size.blockZ()) {
                        for (y in 0 until min(40 - start.blockY(), size.blockY())) {
                            it.modifier().setBlock(start.add(x.toDouble(), y.toDouble(), z.toDouble()), Block.STONE)
                        }
                    }
                }
            }
        }

        node.addListener(PlayerCommandEvent::class.java) { event ->
            val name = PlainTextComponentSerializer.plainText().serialize(event.player.name)
            logger.info("$name using command: /${event.command}")
        }

        node.addListener(PlayerLoginEvent::class.java) { event ->
            event.setSpawningInstance(instance)
            if (spawn == null) {
                event.player.respawnPoint = Pos(0.5, 40.0, 0.5, 0F, 0F)
                return@addListener
            }

            event.player.respawnPoint = spawn!!.loc
        }

        node.addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.player is FakePlayer) {
                return@addListener
            }

            event.player.gameMode = GameMode.ADVENTURE
        }

        node.addListener(PlayerMoveEvent::class.java) { event ->
            if (spawn == null) {
                return@addListener
            }

            AreaUtil(spawn!!.area).apply {
                if (!event.newPosition.inArea()) {
                    event.isCancelled = true
                    return@addListener
                }
            }

            if (spawn!!.height != null) {
                event.player.teleport(spawn!!.loc)
                return@addListener
            }
        }
    }
}