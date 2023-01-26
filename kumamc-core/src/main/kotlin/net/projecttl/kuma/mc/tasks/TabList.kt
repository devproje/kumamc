package net.projecttl.kuma.mc.tasks

import kotlinx.coroutines.*
import net.projecttl.kuma.mc.api.utils.toMini
import net.projecttl.kuma.mc.api.instance

@OptIn(DelicateCoroutinesApi::class)
object TabList {
    fun run() {
        GlobalScope.launch {
            while (isActive) {
                val rt = Runtime.getRuntime()
                instance.sendPlayerListHeaderAndFooter(
                    ("\n <gradient:#00ffff:#0091ff><bold>PROJECT_TL'S PRIVATE SERVER \n" +
                            " <reset><white>프로젝트의 개인 서버에 오신걸 환영합니다! \n").toMini(),
                    ("\n <gray>Using memory: ${String.format("%.2f", (rt.maxMemory() - (rt.maxMemory() - rt.freeMemory())) * 0.001 * 0.001 * 0.001)}GB/${String.format("%.2f", rt.maxMemory() * 0.001 * 0.001 * 0.001)}GB \n" +
                            " Lobby Server powered by <bold><gradient:#ff6c32:#ff76b6>Minestom \n").toMini()
                )

                delay(25)
            }
        }
    }
}


//node.addListener(PlayerEntityInteractEvent::class.java) { event ->
//    entityList.forEach { npc ->
//        if (npc.server == "") {
//            return@addListener
//        }
//
//        if (event.target.customName == npc.name) {
//            event.player.sendMessage("${npc.server}로 이동합니다.")
//            event.player.moveServer(npc.server)
//        }
//    }
//}