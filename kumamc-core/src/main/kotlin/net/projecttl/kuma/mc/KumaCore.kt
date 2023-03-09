package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.velocity.VelocityProxy
import net.minestom.server.instance.InstanceContainer
import net.projecttl.kuma.mc.listener.General
import net.projecttl.kuma.mc.util.PropLoader
import net.projecttl.kuma.mc.util.Props
import net.projecttl.kuma.mc.util.ProxyType

lateinit var prop: Props
lateinit var instance: InstanceContainer

val logger = MinecraftServer.LOGGER!!

class KumaCore {
    private val server = MinecraftServer.init()
    private val handler = MinecraftServer.getGlobalEventHandler()

    fun load() {
        prop = PropLoader.init()
        proxy()

        General.run(handler)
    }

    private fun proxy() = when (prop.proxyType) {
        ProxyType.NONE -> MojangAuth.init()
        ProxyType.BUNGEECORD -> {
            BungeeCordProxy.enable()
            logger.info("Enabled bungeecord forward")
        }

        ProxyType.VELOCITY -> {
            if (prop.secret == "") {
                throw NullPointerException()
            }

            VelocityProxy.enable(prop.secret)
            logger.info("Enabled velocity forward")
        }
    }

    suspend fun run() {
        coroutineScope {
            launch {
                server.start(prop.serverIp, prop.serverPort)
            }
        }
    }
}