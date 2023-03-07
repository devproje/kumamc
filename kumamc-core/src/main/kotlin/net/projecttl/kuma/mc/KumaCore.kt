package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.projecttl.kuma.mc.listener.General
import net.projecttl.kuma.mc.util.PropLoader

lateinit var instance: InstanceContainer

class KumaCore {
    private val server = MinecraftServer.init()

    fun load() {
        PropLoader.init()
        General.run()
    }

    suspend fun run() {
        coroutineScope {
            launch {
                server.start(PropLoader.serverIp, PropLoader.serverPort)
            }
        }
    }
}