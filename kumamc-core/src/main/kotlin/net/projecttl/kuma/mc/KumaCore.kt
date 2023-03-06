package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minestom.server.MinecraftServer

class KumaCore {
    private val server = MinecraftServer.init()


    suspend fun run() {
        coroutineScope {
            launch {

            }
        }
    }
}