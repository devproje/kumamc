package net.projecttl.kuma.mc.runner

import net.projecttl.kuma.mc.Core

suspend fun main() {
    val core = Core().apply {
        mcInit()
        dbInit()
        serverInit()
    }

    core.run()
}