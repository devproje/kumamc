package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.InstanceContainer
import net.projecttl.kuma.mc.listener.General
import net.projecttl.kuma.mc.util.PropLoader
import net.projecttl.kuma.mc.util.Props

lateinit var prop: Props
lateinit var instance: InstanceContainer

var spawn: Pos? = null

class KumaCore {
    private val server = MinecraftServer.init()

    fun load() {
        prop = PropLoader.init()
        General.run()
    }

    suspend fun run() {
        coroutineScope {
            launch {
                server.start(prop.serverIp, prop.serverPort)
            }
        }
    }
}