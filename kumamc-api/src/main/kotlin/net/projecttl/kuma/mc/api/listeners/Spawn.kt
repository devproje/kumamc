package net.projecttl.kuma.mc.api.listeners

import net.minestom.server.color.Color
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.item.firework.FireworkEffect
import net.minestom.server.item.firework.FireworkEffectType
import net.projecttl.kuma.mc.api.utils.Area
import net.projecttl.kuma.mc.api.utils.AreaUtils
import net.projecttl.kuma.mc.api.utils.showFireworkWithDuration
import net.projecttl.kuma.mc.api.utils.toMini
import kotlin.random.Random

class Spawn(val instance: InstanceContainer, val spawn: Pos, val area: Area) {
    fun run(node: EventNode<Event>) {
        node.addListener(PlayerLoginEvent::class.java) { event ->
            event.setSpawningInstance(instance)
            event.player.respawnPoint = spawn
        }

        val messages = listOf(
            "{user}님이 두둥등장!",
            "{user}님이 접속하셨어요!",
            "{user}님, 안녕하세요!",
            "{user}(이)가 서버에 들어오기 아이템을(를) 사용했다!"
        )

        node.addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.player is FakePlayer) {
                return@addListener
            }

            event.player.gameMode = GameMode.ADVENTURE

            val msg = "<bold><aqua>Project_IO's Official Server <reset>${messages.random().replace(
                "{user}", "<bold>${event.player.username}<reset>"
            )}".toMini()

            event.spawnInstance.players.forEach { player ->
                player.sendMessage(msg)
                player.sendMessage("<red>만약 NPC가 겹쳐 보이거나 스폰 위치에 남아 있다면 재접속을 해주시기 바랍니다.<reset>".toMini())
            }

            val effects = mutableListOf(
                FireworkEffect(
                    false, //random.nextBoolean(),
                    false, //random.nextBoolean(),
                    FireworkEffectType.LARGE_BALL,
                    listOf(Color(Random.nextInt(256), 255, 255)),
                    listOf(Color(Random.nextInt(256), 255, 255))
                )
            )
            event.spawnInstance.players.showFireworkWithDuration(instance, spawn, effects)
        }

        node.addListener(PlayerMoveEvent::class.java) { event ->
            if (event.newPosition.y <= 50) {
                event.newPosition = spawn
                return@addListener
            }

            AreaUtils(area = area, height = false).apply {
                if (!event.newPosition.inArea()) {
                    event.isCancelled = true
                    return@addListener
                }
            }
        }
    }
}