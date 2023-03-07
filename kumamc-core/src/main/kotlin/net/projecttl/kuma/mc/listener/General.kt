package net.projecttl.kuma.mc.listener

import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.projecttl.kuma.mc.instance
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

        node.addListener(PlayerLoginEvent::class.java) {
            it.player
        }
    }
}