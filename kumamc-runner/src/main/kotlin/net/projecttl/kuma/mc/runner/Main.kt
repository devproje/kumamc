package net.projecttl.kuma.mc.runner

import net.projecttl.kuma.mc.Core

suspend fun main() {
    val core = Core().apply {
        dbInit()
        mcInit()
        serverInit()
    }

    core.run()
}